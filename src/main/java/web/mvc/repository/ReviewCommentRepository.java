package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.Review;
import web.mvc.domain.ReviewComment;
import web.mvc.dto.ReviewCommentDTO;
import web.mvc.dto.ReviewCommentDetailDTO;

import java.util.List;

public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {

    // 특정 사용자가 작성한 댓글 조회
    @Query(value = "select new web.mvc.dto.ReviewCommentDetailDTO(r.content,r.score,u.name,r.date,r.file.path )from ReviewComment r join User u on r.managementUser.userSeq = u.userSeq where r.reviewCommentSeq=?1")
    List<ReviewCommentDetailDTO> findAllByManagementUser_UserSeq(Long userId);

    // 유저가 단 리뷰 보기
    @Query(value = "select new web.mvc.dto.ReviewCommentDetailDTO(r.content,r.score,u.name,r.date,r.file.path )from ReviewComment r join User u on r.managementUser.userSeq = u.userSeq where r.reviewCommentSeq=?1")
    ReviewCommentDetailDTO findByReviewUser(Long reviewCommentSeq);

    // 업체가 단 리뷰 보기
    @Query(value = "select new web.mvc.dto.ReviewCommentDetailDTO(r.content,r.score,u.comName,r.date,r.file.path)from ReviewComment r join CompanyUser u on r.managementUser.userSeq = u.userSeq where r.reviewCommentSeq=?1")
    ReviewCommentDetailDTO findByReviewCom(Long reviewCommentSeq);
}
