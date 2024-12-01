package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.Review;
import web.mvc.domain.ReviewComment;

import java.util.List;

public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {

    // 특정 사용자가 작성한 댓글 조회
    List<ReviewComment> findAllByManagementUser_UserSeq(Long userId);

    // 특정 리뷰에 대한 댓글 조회
    List<ReviewComment> findAllByReview_ReviewSeq(Long reviewSeq);

}
