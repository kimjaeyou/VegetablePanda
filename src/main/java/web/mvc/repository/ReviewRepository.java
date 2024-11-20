package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.Review;
import web.mvc.domain.ReviewComment;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * 1. 처음에 유저 시퀀스에 해당하는 review 시퀀스를 가져오자
     */
    @Query("select r from Review r where r.managementUser.userSeq = ?1")
    Long selectSeq(Long seq);

    /**
     * 2. 그리고 그 리뷰 시퀀스에 해당하는 정보들을 들고오면 끝
     */
    @Query("select r from Review r LEFT join ReviewComment c on r.reviewSeq = c.review.reviewSeq where r.managementUser.userSeq = ?1")
    List<ReviewComment> review(Long seq);

    /**
     * 리뷰 삭제
     */
    @Modifying
    @Query("Delete from ReviewComment r where r.review.reviewSeq = ?1 ")
    void deleteReview(Long seq);
}
