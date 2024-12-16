package web.mvc.service;

import org.springframework.web.multipart.MultipartFile;
import web.mvc.domain.Review;
import web.mvc.domain.ReviewComment;
import web.mvc.domain.UserBuyDetail;
import web.mvc.dto.ReviewCommentDTO;
import web.mvc.dto.ReviewCommentDetailDTO;
import web.mvc.dto.ReviewCommentStatisticsDTO;

import java.util.List;
import java.util.Optional;

public interface ReviewCommentService {

    // 댓글 등록
    ReviewCommentDTO reviewCommentSave(ReviewComment reviewComment);

    // 댓글 수정
    ReviewCommentDTO reviewCommentUpdate(Long reviewSeq, Long reviewCommentSeq, ReviewCommentDTO reviewCommentDTO, MultipartFile file, boolean deleteFile);

    // 특정 사용자가 작성한 댓글 조회
    List<ReviewCommentDetailDTO> reviewCommentFindAllByUserId(Long userId);

    //유저가 단 리뷰 조회
    ReviewCommentDetailDTO reviewCommentFindAllByReviewId(Long reviewSeq);

    // 댓글 삭제
    void reviewCommentDelete(Long reviewCommentSeq);


    Optional<Review> findByFarmerUserId(Long userSeq);

    // 상품 리뷰 정보 조회
    public List<ReviewCommentDetailDTO> getStockReviewList (Long stockSeq);

    // 상품 리뷰 통계 조회
    public ReviewCommentStatisticsDTO getStockReviewStatistics (Long stockSeq);
}
