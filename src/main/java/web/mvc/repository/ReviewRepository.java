package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.Review;
import web.mvc.domain.ReviewComment;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select c from Review r join ReviewComment c on r.managementUser.userSeq = c.userSeq where c.userSeq = ?1")
    List<ReviewComment> review(Long seq);

    /**
     * 리뷰 삭제
     */
    @Modifying
    @Query("Delete from ReviewComment r where r.review.managementUser.userSeq = ?2 and r.reviewCommentSeq = ?1 ")
    int deleteReview(Long reviewSeq, Long userSeq);
}
