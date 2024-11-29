package web.mvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.ReviewComment;
import web.mvc.dto.ReviewCommentDTO;
import web.mvc.exception.DMLException;
import web.mvc.exception.ErrorCode;
import web.mvc.repository.ReviewCommentRepository;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewCommentServiceImpl implements ReviewCommentService {

    private final ReviewCommentRepository reviewCommentRepository;

    /**
     * 리뷰 등록
     */
    @Override
    public ReviewCommentDTO reviewCommentSave(ReviewComment reviewComment) {
        ReviewComment savedComment = reviewCommentRepository.save(reviewComment);
        return convertToDto(savedComment);
    }

    /**
     * 리뷰 업데이트
     */
    @Override
    public ReviewCommentDTO reviewCommentUpdate(Long reviewCommentSeq, ReviewComment reviewComment) {

        ReviewComment existingComment = reviewCommentRepository.findById(reviewCommentSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_REPLY));

        existingComment.setContent(reviewComment.getContent());
        ReviewComment updatedComment = reviewCommentRepository.save(existingComment);
        return convertToDto(updatedComment);
    }

    /**
     * 내가 해당 판매자에게 쓴 리뷰 전체 조회
     */
    @Override
    public List<ReviewCommentDTO> reviewCommentFindAllById(Long reviewCommentSeq) {
        List<ReviewComment> comments = reviewCommentRepository.findAllByReview_ReviewSeq(reviewCommentSeq);
        return comments.stream()
                .map(this::convertToDto)
                .toList();
    }

    /**
     * 리뷰 삭제
     */

    @Override
    public void reviewCommentDelete(Long reviewCommentSeq) {

        reviewCommentRepository.deleteById(reviewCommentSeq);

    }

    private ReviewCommentDTO convertToDto(ReviewComment reviewComment) {
        return ReviewCommentDTO.builder()
                .reviewCommentSeq(reviewComment.getReviewCommentSeq())
                .content(reviewComment.getContent())
                .score(reviewComment.getScore())
                .build();


    }
}
