package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.mvc.dto.ReviewCommentDTO;
import web.mvc.service.ReviewCommentService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/reviewComment")
public class ReviewCommentController {

    private final ReviewCommentService reviewCommentService;

    /**
     * 리뷰 댓글 저장
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/", consumes = "multipart/form-data")
    public ResponseEntity<?> reviewCommentSave(
            @RequestParam Long reviewSeq,
            @RequestPart ReviewCommentDTO reviewCommentDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        log.info("리뷰 댓글 저장 요청: reviewSeq={}, comment={}", reviewSeq, reviewCommentDTO);

        ReviewCommentDTO savedComment = reviewCommentService.reviewCommentSave(reviewSeq, reviewCommentDTO, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }

    /**
     * 리뷰 댓글 수정
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value = "/{reviewCommentSeq}", consumes = "multipart/form-data")
    public ResponseEntity<?> reviewCommentUpdate(
            @RequestParam Long reviewSeq,
            @PathVariable Long reviewCommentSeq,
            @RequestPart ReviewCommentDTO reviewCommentDTO,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "deleteFile", required = false, defaultValue = "false") boolean deleteFile) {
        log.info("리뷰 댓글 수정 요청: reviewSeq={}, commentSeq={}, comment={}, deleteFile={}", reviewSeq, reviewCommentSeq, reviewCommentDTO, deleteFile);

        ReviewCommentDTO updatedComment = reviewCommentService.reviewCommentUpdate(reviewSeq, reviewCommentSeq, reviewCommentDTO, image, deleteFile);
        return ResponseEntity.ok(updatedComment);
    }

    /**
     * 리뷰 댓글 조회 - 리뷰 ID로 조회
     */
    @GetMapping("/{reviewSeq}")
    public ResponseEntity<?> reviewCommentFindByReviewSeq(@PathVariable Long reviewSeq) {
        log.info("리뷰 댓글 조회 요청: reviewSeq={}", reviewSeq);

        List<ReviewCommentDTO> comments = reviewCommentService.reviewCommentFindAllByReviewId(reviewSeq);
        return ResponseEntity.ok(comments);
    }

    /**
     * 내가 작성한 리뷰 댓글 전체 조회
     */
    @GetMapping("/myComments")
    public ResponseEntity<?> reviewCommentFindByUser() {
        log.info("내가 작성한 리뷰 댓글 조회 요청");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userSeq = Long.parseLong(authentication.getName()); // 사용자 ID 가져오기

        List<ReviewCommentDTO> myComments = reviewCommentService.reviewCommentFindAllByUserId(userSeq);
        return ResponseEntity.ok(myComments);
    }

    /**
     * 리뷰 댓글 삭제
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{reviewCommentSeq}")
    public ResponseEntity<?> reviewCommentDelete(@PathVariable Long reviewCommentSeq) {
        log.info("리뷰 댓글 삭제 요청: commentSeq={}", reviewCommentSeq);

        reviewCommentService.reviewCommentDelete(reviewCommentSeq);
        return ResponseEntity.ok("댓글이 성공적으로 삭제되었습니다.");
    }
}
