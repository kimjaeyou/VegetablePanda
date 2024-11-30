package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import web.mvc.domain.ReviewComment;
import web.mvc.dto.ReviewCommentDTO;
import web.mvc.service.ReviewCommentService;

import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/reviewComment")
public class ReviewCommentController {

    private final ReviewCommentService reviewCommentService;


    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/")
    public ResponseEntity<?> reviewCommentSave(@RequestBody ReviewCommentDTO reviewCommentDTO) {
        ReviewCommentDTO savedComment = reviewCommentService.reviewCommentSave(reviewCommentDTO);
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }

    /**
     * 리뷰 댓글 수정
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/{reviewCommentSeq}")
    public ResponseEntity<?> reviewCommentUpdate(@PathVariable Long reviewCommentSeq, @RequestBody ReviewCommentDTO reviewCommentDTO) {
        ReviewCommentDTO updatedComment = reviewCommentService.reviewCommentUpdate(reviewCommentSeq, reviewCommentDTO);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    /**
     * 리뷰 댓글 조회 - 리뷰 ID로 조회
     */
    @GetMapping("/{reviewSeq}")
    public ResponseEntity<?> reviewCommentFindByReviewSeq(@PathVariable Long reviewSeq) {
        List<ReviewCommentDTO> comments = reviewCommentService.reviewCommentFindAllById(reviewSeq);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    /**
     * 내가 작성한 리뷰 댓글 전체 조회
     */
    @GetMapping("/myComments")
    public ResponseEntity<?> reviewCommentFindByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userSeq = Long.parseLong(authentication.getName());
        List<ReviewCommentDTO> myComments = reviewCommentService.reviewCommentFindAllById(userSeq);
        return new ResponseEntity<>(myComments, HttpStatus.OK);
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
