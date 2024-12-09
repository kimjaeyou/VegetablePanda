package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.Review;
import web.mvc.domain.ReviewComment;
import web.mvc.dto.ReviewCommentDTO;
import web.mvc.dto.ReviewCommentDetailDTO;
import web.mvc.dto.ReviewCommentStatisticsDTO;

import java.util.List;

public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {

    // 특정 사용자가 작성한 댓글 조회
    @Query(value = "select new web.mvc.dto.ReviewCommentDetailDTO(r.reviewCommentSeq, r.managementUser.userSeq ,r.userBuyDetail.stock.product.productName,r.content,r.score,u.name,r.date,r.file.path )from ReviewComment r join User u on r.managementUser.userSeq = u.userSeq where u.userSeq=?1")
    List<ReviewCommentDetailDTO> findAllByManagementUser_UserSeq(Long userId);

    // 유저가 단 리뷰 보기
    @Query(value = "select new web.mvc.dto.ReviewCommentDetailDTO(r.reviewCommentSeq, r.managementUser.userSeq,r.userBuyDetail.stock.product.productName,r.content,r.score,u.name,r.date,r.file.path )from ReviewComment r join User u on r.managementUser.userSeq = u.userSeq where r.reviewCommentSeq=?1")
    ReviewCommentDetailDTO findByReviewUser(Long reviewCommentSeq);

    // 업체가 단 리뷰 보기
    @Query(value = "select new web.mvc.dto.ReviewCommentDetailDTO(r.reviewCommentSeq, r.managementUser.userSeq,r.userBuyDetail.stock.product.productName,r.content,r.score,u.comName,r.date,r.file.path)from ReviewComment r join CompanyUser u on r.managementUser.userSeq = u.userSeq where r.reviewCommentSeq=?1")
    ReviewCommentDetailDTO findByReviewCom(Long reviewCommentSeq);

    // 상품에 대해 작성된 리뷰 조회
    @Query(value = "select new web.mvc.dto.ReviewCommentDetailDTO(r.reviewCommentSeq, r.managementUser.userSeq,r.userBuyDetail.stock.product.productName,r.content,r.score,m.id,r.date,r.file.path, m.file.path) " +
            "from ReviewComment r join ManagementUser m on r.managementUser.userSeq = m.userSeq " +
            "join UserBuyDetail ubd on r.userBuyDetail.userBuySeq = ubd.userBuySeq " +
            "join Stock s on ubd.stock.stockSeq = s.stockSeq where s.stockSeq=?1")
    List<ReviewCommentDetailDTO> findByStockReview (Long stockSeq);

    // 상품에 대한 리뷰 개수와 평균점수
    @Query(value = "select new web.mvc.dto.ReviewCommentStatisticsDTO(avg(r.score), count(distinct r.reviewCommentSeq))" +
            "from ReviewComment r join ManagementUser m on r.managementUser.userSeq = m.userSeq " +
            "join UserBuyDetail ubd on r.userBuyDetail.userBuySeq = ubd.userBuySeq " +
            "join Stock s on ubd.stock.stockSeq = s.stockSeq where s.stockSeq = ?1")
    ReviewCommentStatisticsDTO findStockReviewStatistics (Long stockSeq);
}
