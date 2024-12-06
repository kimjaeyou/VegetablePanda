package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.Review;
import web.mvc.domain.ReviewComment;
import web.mvc.dto.ReviewCommentDTO;
import web.mvc.dto.ReviewCommentDTO2;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * 1. 처음에 유저 시퀀스에 해당하는 review 시퀀스를 가져오자
     */
    @Query("select r.reviewSeq from Review r where r.managementUser.userSeq = ?1")
    Long selectSeq(Long seq);

    /**
     * 2. 그리고 그 리뷰 시퀀스에 해당하는 정보들을 들고오면 끝
     */
    @Query("select new web.mvc.dto.ReviewCommentDTO(c.reviewCommentSeq, c.content, c.score, c.review.reviewSeq) " +
            "from ReviewComment c where c.review.reviewSeq = ?1")
    List<ReviewCommentDTO> review(Long seq);

    /**
     * 리뷰 삭제
     */
    @Modifying
    @Query("Delete from ReviewComment r where r.review.managementUser.userSeq = ?2 and r.reviewCommentSeq = ?1 ")
    int deleteReview(Long reviewSeq, Long userSeq);

    /**
     * 판매자 입장 : 나에게 쓴 리뷰들 전부다 출력
     */
    @Query("select new web.mvc.dto.ReviewCommentDTO2(r.content, " +
            "COALESCE(r.file.path, 'defaultPath'), " + // file.path가 null이면 'defaultPath'를 반환
            "r.score, r.date, r.managementUser.userSeq) " +
            "from ReviewComment r " +
            "left join Review v on r.review.reviewSeq = v.reviewSeq " +
            "left join File f on r.file.fileSeq = f.fileSeq " + // 파일 관련 LEFT JOIN 추가
            "where v.managementUser.userSeq = ?1")
    List<ReviewCommentDTO2> reviewList(Long userSeq);


    /**
     * Personal 페이지 리뷰 5건
     */
    @Query("select new web.mvc.dto.ReviewCommentDTO2(r.content, " +
            "COALESCE(r.file.path, 'defaultPath'), " + // file.path가 null이면 'defaultPath'를 반환
            "r.score, r.date, r.managementUser.userSeq) " +
            "from ReviewComment r " +
            "left join Review v on r.review.reviewSeq = v.reviewSeq " +
            "left join File f on r.file.fileSeq = f.fileSeq " + // 파일 관련 LEFT JOIN 추가
            "where v.managementUser.userSeq = ?1 order by r.date")
    List<ReviewCommentDTO2> reviewListToPersonal(Long userSeq);

}