package web.mvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.ReviewComment;
import web.mvc.dto.ReviewCommentDTO;
import web.mvc.exception.DMLException;
import web.mvc.exception.ErrorCode;
import web.mvc.repository.ReviewCommentRepository;
import web.mvc.repository.ReviewRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewCommentServiceImpl implements ReviewCommentService {

    private final ReviewCommentRepository reviewCommentRepository;
    private final ReviewRepository reviewRepository;

    /**
     * 리뷰 등록
     */
    @Override
    public ReviewCommentDTO reviewCommentSave(ReviewCommentDTO reviewCommentDTO) {
        ReviewComment reviewComment = reviewCommentDTO.toEntity();
        reviewComment.setReview(reviewRepository.findById(reviewCommentDTO.getReviewSeq())
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_REPLY)));
        ReviewComment savedComment = reviewCommentRepository.save(reviewComment);
        return ReviewCommentDTO.fromEntity(savedComment);
    }

    /**
     * 리뷰 업데이트
     */
    @Override
    public ReviewCommentDTO reviewCommentUpdate(Long reviewCommentSeq, ReviewCommentDTO reviewCommentDTO) {
        ReviewComment comment = reviewCommentRepository.findById(reviewCommentSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_REPLY));

        comment.setContent(reviewCommentDTO.getContent());
        comment.setScore(reviewCommentDTO.getScore());

        ReviewComment updatedComment = reviewCommentRepository.save(comment);
        return ReviewCommentDTO.fromEntity(updatedComment);
    }

    /**
     * 내가 쓴 리뷰 전체 조회
     */
    @Override
    public List<ReviewCommentDTO> reviewCommentFindAllById(Long userId) {
        List<ReviewComment> comments = reviewCommentRepository.findAllByManagementUser_UserSeq(userId);

        return comments.stream()
                .map(ReviewCommentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 리뷰 삭제
     */

    @Override
    public void reviewCommentDelete(Long reviewCommentSeq) {
        if (!reviewCommentRepository.existsById(reviewCommentSeq)) {
            throw new DMLException(ErrorCode.NOTFOUND_REPLY);
        }
        reviewCommentRepository.deleteById(reviewCommentSeq);

    }
}
