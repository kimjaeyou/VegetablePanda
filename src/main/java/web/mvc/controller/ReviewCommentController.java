package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
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
    @PostMapping("/")
    public ResponseEntity<?> reviewCommentSave(@RequestParam Long reviewSeq, @RequestBody ReviewCommentDTO reviewCommentDTO) {
        ReviewCommentDTO savedComment = reviewCommentService.reviewCommentSave(reviewSeq, reviewCommentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }

    /**
     * 리뷰 댓글 수정
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/{reviewCommentSeq}")
    public ResponseEntity<?> reviewCommentUpdate(@RequestParam Long reviewSeq, @PathVariable Long reviewCommentSeq, @RequestBody ReviewCommentDTO reviewCommentDTO) {
        ReviewCommentDTO updatedComment = reviewCommentService.reviewCommentUpdate(reviewSeq, reviewCommentSeq, reviewCommentDTO);
        return ResponseEntity.ok(updatedComment);
    }

    /**
     * 리뷰 댓글 조회 - 리뷰 ID로 조회
     */
    @GetMapping("/{reviewSeq}")
    public ResponseEntity<?> reviewCommentFindByReviewSeq(@PathVariable Long reviewSeq) {
        List<ReviewCommentDTO> comments = reviewCommentService.reviewCommentFindAllByReviewId(reviewSeq);
        return ResponseEntity.ok(comments);
    }

    /**
     * 내가 작성한 리뷰 댓글 전체 조회
     */
    @GetMapping("/myComments")
    public ResponseEntity<?> reviewCommentFindByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userSeq = Long.parseLong(authentication.getName()); // 사용자 ID를 가져옴
        List<ReviewCommentDTO> myComments = reviewCommentService.reviewCommentFindAllByUserId(userSeq);
        return ResponseEntity.ok(myComments);
    }

    /**
     * 리뷰 댓글 삭제
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{reviewCommentSeq}")
    public ResponseEntity<?> reviewCommentDelete(@PathVariable Long reviewCommentSeq) {
        reviewCommentService.reviewCommentDelete(reviewCommentSeq);
        return ResponseEntity.ok("댓글이 성공적으로 삭제되었습니다.");
    }
}
