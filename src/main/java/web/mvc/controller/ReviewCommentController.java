package web.mvc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.mvc.domain.*;
import web.mvc.dto.ReviewCommentDTO;
import web.mvc.dto.ReviewCommentDetailDTO;
import web.mvc.dto.StockDTO;
import web.mvc.exception.DMLException;
import web.mvc.exception.ErrorCode;
import web.mvc.security.CustomMemberDetails;
import web.mvc.service.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ReviewCommentController {

    private final ReviewCommentService reviewCommentService;
    private final UserBuyDetailService userBuyDetailService;
    private final ReviewService reviewService;
    private final ObjectMapper objectMapper;
    private final FileService fileService;
    private final S3ImageService s3ImageService;

    @Transactional
    @PostMapping(value = "/reviewComment/all", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveReviewComment(
            @RequestPart ReviewCommentDTO reviewCommentDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) throws JsonProcessingException {

        ReviewComment reviewComment = new ReviewComment();
        reviewComment.setReview(new Review(reviewCommentDTO.getReviewSeq()));
        reviewComment.setContent(reviewCommentDTO.getContent());
        reviewComment.setScore(reviewCommentDTO.getScore());
        reviewComment.setUserBuyDetail(new UserBuyDetail(reviewCommentDTO.getUserBuyDetailSeq()));
        reviewComment.setManagementUser(new ManagementUser(reviewCommentDTO.getUserSeq()));
        reviewComment.setFile(null);

        // 파일 업로드
        if(image != null) {
            String reviewImage = s3ImageService.upload(image);
            log.info("file생성 - stockImage : {}", reviewImage);

            // File 객체 생성 및 저장
            File newFile = new File(reviewImage, reviewCommentDTO.getFile().getName());
            File file = fileService.save(newFile);
            reviewComment.setFile(file);
        }
         ReviewCommentDTO result = reviewCommentService.reviewCommentSave(reviewComment);

//        ReviewCommentDTO reviewCommentDTO = objectMapper.readValue(reviewCommentDTOJson, ReviewCommentDTO.class);
//
//        Long userSeq = getAuthenticatedUserSeq(); // 인증된 사용자 ID
//        UserBuyDetail userBuyDetail = userBuyDetailService.findLatestByUserSeq(userSeq)
//                .orElseThrow(() -> new DMLException(ErrorCode.ORDER_NOTFOUND));
//
//        Stock stock = Stock.getStock();
//        FarmerUser farmerUser = stock.getFarmerUser();
//
//
//        Review review = reviewService.findByManagementUserId(farmerUser.getUserSeq())
//                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));
//
//        ReviewCommentDTO savedComment = reviewCommentService.reviewCommentSave(review, userBuyDetail, reviewCommentDTO, file);
//
       return  ResponseEntity.status(HttpStatus.CREATED).body(result);
    }


    /**
     * 리뷰 댓글 수정
     */
    @PutMapping(value = "/reviewComment/{reviewCommentSeq}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> reviewCommentUpdate(
            @RequestParam Long reviewSeq,
            @PathVariable Long reviewCommentSeq,
            @RequestPart(value = "reviewCommentDTO") String reviewCommentDTOJson,
            @RequestPart(value = "image", required = false) MultipartFile file,
            @RequestParam(value = "deleteFile", required = false, defaultValue = "false") boolean deleteFile) throws JsonProcessingException {

        // JSON 문자열 -> DTO 변환
        ObjectMapper objectMapper = new ObjectMapper();
        ReviewCommentDTO reviewCommentDTO = objectMapper.readValue(reviewCommentDTOJson, ReviewCommentDTO.class);

        ReviewCommentDTO updatedComment = reviewCommentService.reviewCommentUpdate(reviewSeq, reviewCommentSeq, reviewCommentDTO, file, deleteFile);
        return ResponseEntity.ok(updatedComment);
    }

    /**
     * 내가 쓴 리뷰 조회
     * 정상 작동함
     */
    @GetMapping("/reviewComment/{reviewCommentSeq}")
    public ResponseEntity<?> reviewCommentFindByReviewSeq(@PathVariable Long reviewCommentSeq) {
        ReviewCommentDetailDTO review = reviewCommentService.reviewCommentFindAllByReviewId(reviewCommentSeq);
        return ResponseEntity.ok(review);
    }

    
    /**
     * 내가 작성한 리뷰 조회
     *
     * */
    @GetMapping("/myComments/{userSeq}")
    public ResponseEntity<?> reviewCommentFindByUser(@PathVariable Long userSeq) {
        log.info("내가 작성한 리뷰 댓글 전체 조회 요청");

        log.info("현재 사용자 ID: {}", userSeq);

        // 서비스 계층 호출
        List<ReviewCommentDetailDTO> myComments = reviewCommentService.reviewCommentFindAllByUserId(userSeq);

        // 결과 반환
        return ResponseEntity.ok(myComments);
    }

    /**
     * 리뷰 상세 조회 만들어야 할듯?
     * */

    /**
     * 리뷰 댓글 삭제
     * 정상 작동함
     */
    @DeleteMapping("/reviewComment/{reviewCommentSeq}")
    public ResponseEntity<?> reviewCommentDelete(@PathVariable Long reviewCommentSeq) {

        reviewCommentService.reviewCommentDelete(reviewCommentSeq);
        return ResponseEntity.ok("댓글이 성공적으로 삭제되었습니다.");
    }

    /**
     * 현재 인증된 사용자의 userSeq를 반환
     */
    private Long getAuthenticatedUserSeq() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Long.parseLong(authentication.getName());
    }
}
