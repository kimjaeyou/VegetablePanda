package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.mvc.dto.*;
import web.mvc.repository.FileRepository;
import web.mvc.service.S3ImageService;
import web.mvc.service.UserMyPageService;

import java.util.List;


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
    public ResponseEntity<List<UserBuyListForReivewDTO>> buyList(@PathVariable Long seq) {
        log.info("주문 내역 조회 컨트롤러 시작 - userSeq: {}", seq);

        try {
            List<UserBuyListForReivewDTO> list = userMyPageService.buyList(seq);
            log.info("주문 내역 조회 결과 - 건수: {}", list.size());
            log.info("주문 내역 상세: {}", list);

            if (list.isEmpty()) {
                log.info("주문 내역이 없습니다 - userSeq: {}", seq);
            }

            return ResponseEntity.ok(list);
        } catch (Exception e) {
            log.error("주문 내역 조회 중 오류 발생 - userSeq: {}", seq, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/companyBuyList/{seq}")
    public ResponseEntity<List<UserBuyListForReivewDTO>> companyAuctionBuyList(@PathVariable Long seq) {
        log.info("업체 경매 구매 내역 조회 컨트롤러 시작 - companySeq: {}", seq);

        try {
            List<UserBuyListForReivewDTO> list = userMyPageService.companyAuctionBuyList(seq);
            log.info("업체 경매 구매 내역 조회 결과 - 건수: {}", list.size());
            log.info("업체 경매 구매 내역 상세: {}", list);

            if (list.isEmpty()) {
                log.info("업체 경매 구매 내역이 없습니다 - companySeq: {}", seq);
            }

            return ResponseEntity.ok(list);
        } catch (Exception e) {
            log.error("업체 경매 구매 내역 조회 중 오류 발생 - companySeq: {}", seq, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
                return new ResponseEntity<>(userMyPageService.update(getAllUserDTO, seq), HttpStatus.OK);
            }
            if (file != null) { // 있으면 값 지우기
                String path = fileRepository.selectFile(getAllUserDTO.getId());
                s3ImageService.deleteImageFromS3(path);
                fileRepository.deletePath(getAllUserDTO.getId());
            }
        }
        return new ResponseEntity<>(userMyPageService.update(getAllUserDTO, seq), HttpStatus.OK);
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
     * 경매 낙찰내역 조회
     */
    @GetMapping("/auction/result/{seq}")
    public ResponseEntity<List<BidAuctionDTO>> successfulBid (@PathVariable Long seq) {
        return new ResponseEntity(userMyPageService.successfulBidList(seq), HttpStatus.OK);
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