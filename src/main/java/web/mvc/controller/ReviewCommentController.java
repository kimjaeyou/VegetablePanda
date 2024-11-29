package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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



    @PostMapping("/")
    public ResponseEntity<?> reviewCommentSave(@RequestBody ReviewCommentDTO reviewCommentDTO) {
        validateRole();
        ReviewCommentDTO savedComment = reviewCommentService.reviewCommentSave(reviewCommentDTO);
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }

    /**
     * 리뷰 댓글 수정
     */
    @PutMapping("/{reviewCommentSeq}")
    public ResponseEntity<?> reviewCommentUpdate(@PathVariable Long reviewCommentSeq, @RequestBody ReviewCommentDTO reviewCommentDTO) {
        validateRole();
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
        validateRole();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userSeq = Long.parseLong(authentication.getName());
        List<ReviewCommentDTO> myComments = reviewCommentService.reviewCommentFindAllById(userSeq);
        return new ResponseEntity<>(myComments, HttpStatus.OK);
    }

    /**
     * 리뷰 댓글 삭제
     */
    @DeleteMapping("/{reviewCommentSeq}")
    public ResponseEntity<?> reviewCommentDelete(@PathVariable Long reviewCommentSeq) {
        validateRole();
        reviewCommentService.reviewCommentDelete(reviewCommentSeq);
        return ResponseEntity.ok("댓글이 성공적으로 삭제되었습니다.");
    }

    /**
     * 사용자 권한 검증
     */
    private void validateRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isUser = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"));
        if (!isUser) {
            throw new SecurityException("사용자 권한이 없습니다.");
        }
    }

}
