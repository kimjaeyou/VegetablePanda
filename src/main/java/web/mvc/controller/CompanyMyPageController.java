package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.mvc.domain.Bid;
import web.mvc.domain.CompanyUser;
import web.mvc.domain.ReviewComment;
import web.mvc.dto.GetAllUserDTO;
import web.mvc.dto.ReviewCommentDTO;
import web.mvc.dto.UserBuyDTO;
import web.mvc.repository.FileRepository;
import web.mvc.service.CompanyMyPageService;
import web.mvc.service.S3ImageService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/myPage/company")
public class CompanyMyPageController {

    private final CompanyMyPageService companyMyPageService;
    private final S3ImageService s3ImageService;
    private final FileRepository fileRepository;

    /**
     * 회원의 정보 조회
     */
    @GetMapping("/list/{seq}")
    public ResponseEntity<?> selectUser(@PathVariable Long seq) {
        return new ResponseEntity<>(companyMyPageService.selectUser(seq), HttpStatus.OK);
    }

    /**
     * 회원정보 수정
     */
    @PutMapping("/update/{seq}")
    public ResponseEntity<?> update(
            @PathVariable Long seq,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart("companyData") GetAllUserDTO getAllUserDTO) {

        log.info("id={}", getAllUserDTO.getId());
        log.info("image = {}", image);
        log.info("path1 = {}", getAllUserDTO.getPath());

        // 파일 업로드 시작
        if (image != null) { // 이미지 값이 있는상태로 전송되었다면 경우의수를 추적한다.
            String path2 = fileRepository.selectFile(getAllUserDTO.getId());
            log.info("path = {}", path2);
            if (path2 == null) { // 만약 DB에 값이 없으면 바로 업로드 시키고
                String pathUpdate = s3ImageService.upload(image);
                log.info("pathUpdate = {}", pathUpdate);
                fileRepository.updatePath(pathUpdate, getAllUserDTO.getId());
            } else { // DB에 값이 있으면 수정하는거니까 일단 지우고 업로드
                s3ImageService.deleteImageFromS3(path2);
                String pathUpdate = s3ImageService.upload(image);
                log.info("pathUpdate = {}", pathUpdate);
                // String id = managementRepository.findId(seq);
                fileRepository.updatePath(pathUpdate, getAllUserDTO.getId());
            }
        } else if (image == null) { // 근데 이미지값이 없으면 경우의 수 추적
            String file = fileRepository.selectFile(getAllUserDTO.getId()); // 일단 path가 있는지 검색
            String path3 = getAllUserDTO.getPath(); // userData에서 path 받기
            log.info("path3 = {}", path3);
            if (path3 != null) {
                fileRepository.updatePath(path3, getAllUserDTO.getId());
                return new ResponseEntity<>(companyMyPageService.update(getAllUserDTO, seq), HttpStatus.OK);
            }
            if (file != null) { // 있으면 값 지우기
                String path = fileRepository.selectFile(getAllUserDTO.getId());
                s3ImageService.deleteImageFromS3(path);
                fileRepository.deletePath(getAllUserDTO.getId());
            }
        }
        return new ResponseEntity<>(companyMyPageService.update(getAllUserDTO, seq), HttpStatus.OK);
    }
    /**
     * 탈퇴 (계정 상태 변경)
     */
    @PutMapping("/delete/{seq}")
    public int delete(@PathVariable Long seq) {
        return companyMyPageService.delete(seq);
    }

}