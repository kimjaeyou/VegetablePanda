package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.mvc.domain.*;
import web.mvc.dto.*;
import web.mvc.repository.FileRepository;
import web.mvc.repository.ManagementRepository;
import web.mvc.service.CompanyMyPageService;
import web.mvc.service.FileService;
import web.mvc.service.S3ImageService;
import web.mvc.service.UserMyPageService;

import java.util.List;

import static web.mvc.domain.QManagementUser.managementUser;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/myPage")
public class UserMyPageController {
    private final UserMyPageService userMyPageService;
    private final S3ImageService s3ImageService;
    private final FileRepository fileRepository;
    /**
     * 주문내역 조회
     */
    @GetMapping("/buyList/{seq}")
    public ResponseEntity<List<UserBuyDTO>> buyList(@PathVariable Long seq) {
        List<UserBuyDTO> list = userMyPageService.buyList(seq);
        log.info("UserBuyDTO = {}", list);
        return ResponseEntity.ok(list);
    }

    /**
     * 회원의 정보 조회
     */
    @GetMapping("/list/{seq}")
    public ResponseEntity<?> selectUser(@PathVariable Long seq) {
        return new ResponseEntity<>(userMyPageService.selectUser(seq), HttpStatus.OK);
    }

    /**
     * 회원정보 수정
     */
    @PutMapping("/user/update/{seq}")
    public ResponseEntity<?> update(
            @RequestPart(value = "image", required = false) MultipartFile image,
            @PathVariable Long seq,
            @RequestPart("userData") GetAllUserDTO getAllUserDTO) {

        log.info("id = {}", getAllUserDTO.getId());
        log.info("image = {}", image);

        // 파일 업로드
        if(image != null) { // 이미지 값이 있는상태로 전송되었다면 경우의수를 추적한다.
            String path = fileRepository.selectFile(getAllUserDTO.getId());
            log.info("path = {}", path);
            if (path == null) { // 만약 DB에 값이 없으면 바로 업로드 시키고
                String pathUpdate = s3ImageService.upload(image);
                log.info("pathUpdate = {}", pathUpdate);
                fileRepository.updatePath(pathUpdate, getAllUserDTO.getId());
            } else { // DB에 값이 있으면 수정하는거니까 일단 지우고 업로드
                s3ImageService.deleteImageFromS3(path);
                String pathUpdate = s3ImageService.upload(image);
                log.info("pathUpdate = {}", pathUpdate);
                // String id = managementRepository.findId(seq);
                fileRepository.updatePath(pathUpdate, getAllUserDTO.getId());
            }
        } else if (image == null) { // 근데 이미지값이 없으면 경우의 수 추적
            String file = fileRepository.selectFile(getAllUserDTO.getId()); // 일단 path가 있는지 검색

            if ( file != null ) { // 있으면 값 지우기
            String path = fileRepository.selectFile(getAllUserDTO.getId());
            s3ImageService.deleteImageFromS3(path);
            fileRepository.deletePath(getAllUserDTO.getId());
            }
        }
        return new ResponseEntity<>( userMyPageService.update(getAllUserDTO, seq), HttpStatus.OK);
    }

    /**
     * 탈퇴 (계정 상태 변경)
     */
    @PutMapping("/delete/{seq}")
    public int delete(@PathVariable Long seq) {
        return userMyPageService.delete(seq);
    }


    /**
     * 지갑 잔액 조회
     */
    @GetMapping("/point/{seq}")
    public ResponseEntity<Integer> point(@PathVariable Long seq) {
        int point = userMyPageService.point(seq);
        log.info("point = {}", point);
        return ResponseEntity.ok(point);
    }

    /**
     * 나의 활동내역 리스트 - 리뷰 조회
     */
    @GetMapping("/review/{seq}")
    public ResponseEntity<List<ReviewCommentDTO>> review(@PathVariable Long seq) {
        List<ReviewCommentDTO> list = userMyPageService.review(seq);
        log.info("list = {}", list);
        return ResponseEntity.ok(list);
    }

    /**
     * 리뷰 삭제
     */
    @DeleteMapping("/review/{userSeq}/{reviewSeq}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewSeq, @PathVariable Long userSeq) {
        userMyPageService.deleteReview(reviewSeq, userSeq);
        return ResponseEntity.ok("리뷰 삭제 완료");
    }

    /**
     * 경매 참여내역 조회
     */
    @GetMapping("/auction/{seq}")
    public ResponseEntity<List<BidAuctionDTO>> auctionList(@PathVariable Long seq) {
        return new ResponseEntity<> (userMyPageService.auctionList(seq), HttpStatus.OK);
    }

    /**
     * 상품 좋아요 목록
     */
    @GetMapping("/like/{seq}")
    public ResponseEntity<List<LikeDTO>> likeList(@PathVariable Long seq) {
        return new ResponseEntity<>( userMyPageService.likeList(seq), HttpStatus.OK);
    }

    /**
     * 좋아요 취소
     */
    @DeleteMapping("/like/delete/{seq}")
    public String likeDelete(@PathVariable Long seq, @PathVariable Long likeSeq) {

        return userMyPageService.likeDelete(seq, likeSeq);
    }

    /**
     * 판매자 구독 목록
     */
    @GetMapping("/userLike/{seq}")
    public ResponseEntity<?> userLikeList(@PathVariable Long seq) {
        return new ResponseEntity<>(userMyPageService.userLikeList(seq), HttpStatus.OK);

    }
    /**
     * 판매자 구독 취소
     */
    @DeleteMapping("/userLike/delete/{seq}")
    public String userLikeDelete(@PathVariable Long seq, @PathVariable Long userLikeSeq) {
        return userMyPageService.userLikeDelete(seq, userLikeSeq);
    }

}