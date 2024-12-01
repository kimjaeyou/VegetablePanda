package web.mvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.Review;
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

    private Review findReviewById(Long reviewSeq) {
        return reviewRepository.findById(reviewSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));
    }

    private ReviewComment findCommentById(Long reviewCommentSeq) {
        return reviewCommentRepository.findById(reviewCommentSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_REPLY));
    }

    /**
     * 댓글 등록
     */
    @Override
    public ReviewCommentDTO reviewCommentSave(Long reviewSeq, ReviewCommentDTO reviewCommentDTO) {
        if (reviewCommentDTO == null) {
            throw new IllegalArgumentException("ReviewCommentDTO cannot be null");
        }

        ReviewComment reviewComment = reviewCommentDTO.toEntity();
        reviewComment.setReview(findReviewById(reviewSeq));

        ReviewComment savedComment = reviewCommentRepository.save(reviewComment);
        return ReviewCommentDTO.fromEntity(savedComment);
    }

    /**
     * 댓글 수정
     */
    @Override
    public ReviewCommentDTO reviewCommentUpdate(Long reviewSeq, Long reviewCommentSeq, ReviewCommentDTO reviewCommentDTO) {
        if (reviewCommentDTO == null) {
            throw new IllegalArgumentException("ReviewCommentDTO cannot be null");
        }

        ReviewComment comment = findCommentById(reviewCommentSeq);

        comment.setContent(reviewCommentDTO.getContent());
        comment.setScore(reviewCommentDTO.getScore());

        ReviewComment updatedComment = reviewCommentRepository.save(comment);
        return ReviewCommentDTO.fromEntity(updatedComment);
    }

    /**
     * 특정 사용자가 작성한 댓글 조회
     */
    @Override
    @Transactional(readOnly = true)
    public List<ReviewCommentDTO> reviewCommentFindAllByUserId(Long userId) {
        List<ReviewComment> comments = reviewCommentRepository.findAllByManagementUser_UserSeq(userId);
        return comments.stream()
                .map(ReviewCommentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 특정 게시글에 대한 댓글 조회
     */
    @Override
    @Transactional(readOnly = true)
    public List<ReviewCommentDTO> reviewCommentFindAllByReviewId(Long reviewSeq) {
        List<ReviewComment> comments = reviewCommentRepository.findAllByReview_ReviewSeq(reviewSeq);
        return comments.stream()
                .map(ReviewCommentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 댓글 삭제
     */
    @Override
    public void reviewCommentDelete(Long reviewCommentSeq) {
        ReviewComment comment = findCommentById(reviewCommentSeq);
        reviewCommentRepository.deleteById(comment.getReviewCommentSeq());
    }
}
