package web.mvc.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


/**
 * https://innovation123.tistory.com/197 참고
 * */
@Slf4j
@RequiredArgsConstructor
@Component
public class S3ImageService {

  private final AmazonS3 amazonS3;

  @Value("${cloud.aws.s3.bucketName}")
  private String bucketName;

  /**
   * 외부에서 사용할 public 메서드이고, S3에 저장된 이미지 객체의 public url을 반환한다.
   * */
  public String upload(MultipartFile image) {
    if(image.isEmpty() || Objects.isNull(image.getOriginalFilename())){
      //throw new S3Exception(ErrorCode.EMPTY_FILE_EXCEPTION);
      throw new RuntimeException("버킷 EMPTY_FILE_EXCEPTION");
    }
    return this.uploadImage(image);
  }

  /**
   * validateImageFileExtention()을 호출하여 확장자 명이 올바른지 확인한다.
   *
   * 2. uploadImageToS3()를 호출하여 이미지를 S3에 업로드하고,
   * S3에 저장된 이미지의 public url을 받아서 서비스 로직에 반환한다.
   * */
  private String uploadImage(MultipartFile image) {
    this.validateImageFileExtention(image.getOriginalFilename());
    try {
      return this.uploadImageToS3(image);
    } catch (IOException e) {
      //throw new S3Exception(ErrorCode.IO_EXCEPTION_ON_IMAGE_UPLOAD);
      throw new RuntimeException("버킷 IO_EXCEPTION_ON_IMAGE_UPLOAD");
    }
  }

  /**
   * filename을 받아서 파일 확장자가 jpg, jpeg, png, gif 중에 속하는지 검증한다.
   * */
  private void validateImageFileExtention(String filename) {
    int lastDotIndex = filename.lastIndexOf(".");
    if (lastDotIndex == -1) {
      //throw new S3Exception(ErrorCode.NO_FILE_EXTENTION);
      throw new RuntimeException("버킷 NO_FILE_EXTENTION");
    }

    String extention = filename.substring(lastDotIndex + 1).toLowerCase();
    List<String> allowedExtentionList = Arrays.asList("jpg", "jpeg", "PNG","png", "gif", "webp");

    if (!allowedExtentionList.contains(extention)) {
      //throw new S3Exception(ErrorCode.INVALID_FILE_EXTENTION);
      throw new RuntimeException("버킷 INVALID_FILE_EXTENTION");
    }
  }

  /**
   * 직접적으로 S3에 업로드하는 메서드이다.
   * */
  private String uploadImageToS3(MultipartFile image) throws IOException {
    String originalFilename = image.getOriginalFilename(); //원본 파일 명
    String extention = originalFilename.substring(originalFilename.lastIndexOf(".")); //확장자 명

    String s3FileName = UUID.randomUUID().toString().substring(0, 10) + originalFilename; //변경된 파일 명

    InputStream is = image.getInputStream();
    byte[] bytes = IOUtils.toByteArray(is);

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentType(getContentType(extention)); // 올바른 Content-Type 설정
    metadata.setContentLength(bytes.length);
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

    try{
      System.out.println("bucketName = " + bucketName);
      System.out.println("s3FileName = " + s3FileName);
      System.out.println("byteArrayInputStream = " + byteArrayInputStream);
      System.out.println("metadata = " + metadata);

      PutObjectRequest putObjectRequest =
          new PutObjectRequest(bucketName, s3FileName, byteArrayInputStream, metadata);
              //.withCannedAcl(CannedAccessControlList.PublicRead);
      System.out.println("1111111111111");

      amazonS3.putObject(putObjectRequest); // put image to S3
      System.out.println("222222222222222");
    }catch (Exception e){
      e.printStackTrace();
      //throw new S3Exception(ErrorCode.PUT_OBJECT_EXCEPTION);
      throw new RuntimeException("버킷 PUT_OBJECT_EXCEPTION");
    }finally {
      byteArrayInputStream.close();
      is.close();
    }

    return amazonS3.getUrl(bucketName, s3FileName).toString();
  }


  /**
   * 이미지의 public url을 이용하여 S3에서 해당 이미지를 제거하는 메서드이다.
   * getKeyFromImageAddress()를 호출하여 삭제에 필요한 key를 얻는다.
   * */
  public void deleteImageFromS3(String imageAddress){
    String key = getKeyFromImageAddress(imageAddress);
    try{
      amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
    }catch (Exception e){
      //throw new S3Exception(ErrorCode.IO_EXCEPTION_ON_IMAGE_DELETE);
      throw new RuntimeException("버킷 IO_EXCEPTION_ON_IMAGE_DELETE");
    }
  }
  /**
   * 확장자에 따른 올바른 Content-Type을 반환하는 메서드
   */
  private String getContentType(String extention) {
    switch (extention) {
      case "jpg":
      case "jpeg":
        return "image/jpeg";
      case "png":
        return "image/png";
      case "gif":
        return "image/gif";
      default:
        // 지원하지 않는 확장자일 경우 기본 Content-Type 설정
        return "application/octet-stream";
    }
  }
  private String getKeyFromImageAddress(String imageAddress){
    try{
      URL url = new URL(imageAddress);
      String decodingKey = URLDecoder.decode(url.getPath(), "UTF-8");
      return decodingKey.substring(1); // 맨 앞의 '/' 제거
    }catch (MalformedURLException | UnsupportedEncodingException e){
      //throw new S3Exception(ErrorCode.IO_EXCEPTION_ON_IMAGE_DELETE);
      throw new RuntimeException("버킷 IO_EXCEPTION_ON_IMAGE_DELETE");
    }
  }

  public byte[] downloadFile(String s3Path) {
    try {
      String key = getKeyFromImageAddress(s3Path);

      S3Object s3Object = amazonS3.getObject(bucketName, key);
      S3ObjectInputStream inputStream = s3Object.getObjectContent();

      try {
        byte[] content = IOUtils.toByteArray(inputStream);
        return content;
      } finally {
        // 리소스 정리
        inputStream.close();
      }

    } catch (AmazonS3Exception e) {
      log.error("S3 파일 다운로드 실패 - S3 오류: {}", e.getMessage());
      throw new RuntimeException("파일 다운로드 중 S3 오류가 발생했습니다.");
    } catch (IOException e) {
      log.error("S3 파일 다운로드 실패 - IO 오류: {}", e.getMessage());
      throw new RuntimeException("파일 다운로드 중 IO 오류가 발생했습니다.");
    }
  }
}