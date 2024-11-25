package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import web.mvc.domain.QaBoardReply;
import web.mvc.domain.Review;
import web.mvc.domain.ReviewComment;
import web.mvc.service.ReviewCommentService;

import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/ReviewComment")
public class ReviewCommentController {

    private final ReviewCommentService reviewCommentService;



    /**
     * 리뷰 등록
     */
    @PostMapping("/")
    public ResponseEntity<?> reviewCommentSave (@RequestBody ReviewComment reviewComment) {
        validateAdminRole();
        return new ResponseEntity<>(reviewCommentService.reviewCommentSave(reviewComment), HttpStatus.CREATED);
    }

    /**
     * 리뷰 수정
     */
    @PutMapping("/{reviewCommentSeq}")
    public ResponseEntity<?> reviewCommentUpdate (@PathVariable Long reviewCommentSeq, @RequestBody ReviewComment reviewComment) {
        validateAdminRole();
        return new ResponseEntity<>(reviewCommentService.reviewCommentUpdate(reviewCommentSeq, reviewComment), HttpStatus.OK);
    }


    /**
     * 리뷰 게시글 별 조회
     */
    @GetMapping("/{reviewCommentSeq}")
    public ResponseEntity<?> reviewCommentFindAllById (@PathVariable Long  reviewSeq) {
        List<ReviewComment> rc = (List<ReviewComment>) reviewCommentService.reviewCommentFindAllById(reviewSeq);

        return new ResponseEntity<>(rc, HttpStatus.OK);
    }

    /**
     * 리뷰 삭제
     */
    @DeleteMapping("/{reviewCommentSeq}")
    public ResponseEntity<?> reviewCommentDelete (@PathVariable Long reviewCommentSeq) {
        validateAdminRole();
        reviewCommentService.reviewCommentDelete(reviewCommentSeq);
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }


    private void validateAdminRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean user = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"));
        if (!user) {
            throw new SecurityException("적절한 사용자가 아닙니다.");
        }
    }

}
