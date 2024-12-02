package web.mvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import web.mvc.domain.File;
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
@Slf4j
public class ReviewCommentServiceImpl implements ReviewCommentService {

    private final ReviewCommentRepository reviewCommentRepository;
    private final ReviewRepository reviewRepository;
    private final S3ImageService s3ImageService;

    /**
     * 댓글 등록
     */
    @Override
    public ReviewCommentDTO reviewCommentSave(Long reviewSeq, ReviewCommentDTO reviewCommentDTO, MultipartFile image) {

        // 리뷰 정보 가져오기
        Review review = findReviewById(reviewSeq);

        // DTO -> 엔티티 변환
        ReviewComment reviewComment = reviewCommentDTO.toEntity();
        reviewComment.setReview(review);

        // 파일 업로드 처리
        if (image != null && !image.isEmpty()) {
            reviewComment.setFile(uploadFileToS3(image));
        }

        // 댓글 저장
        ReviewComment savedComment = reviewCommentRepository.save(reviewComment);
        return ReviewCommentDTO.fromEntity(savedComment);
    }

    /**
     * 댓글 수정
     */
    @Override
    public ReviewCommentDTO reviewCommentUpdate(Long reviewSeq, Long reviewCommentSeq, ReviewCommentDTO reviewCommentDTO, MultipartFile image, boolean deleteFile) {

        // 기존 댓글 및 리뷰 정보 가져오기
        ReviewComment existingComment = findCommentById(reviewCommentSeq);
        Review review = findReviewById(reviewSeq);

        // 파일 삭제 요청 처리
        if (deleteFile && existingComment.getFile() != null) {
            deleteFileFromS3(existingComment.getFile());
            existingComment.setFile(null);
        }

        // 새로운 파일 업로드 처리
        if (image != null && !image.isEmpty()) {
            if (existingComment.getFile() != null) {
                deleteFileFromS3(existingComment.getFile());
            }
            existingComment.setFile(uploadFileToS3(image));
        }

        // 댓글 내용 및 평점 수정
        existingComment.setContent(reviewCommentDTO.getContent());
        existingComment.setScore(reviewCommentDTO.getScore());
        existingComment.setReview(review);

        ReviewComment updatedComment = reviewCommentRepository.save(existingComment);
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

        // 파일 삭제 처리
        if (comment.getFile() != null) {
            deleteFileFromS3(comment.getFile());
        }

        reviewCommentRepository.deleteById(reviewCommentSeq);
    }

    /**
     * 리뷰 ID로 조회
     */
    private Review findReviewById(Long reviewSeq) {
        return reviewRepository.findById(reviewSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));
    }

    /**
     * 댓글 ID로 조회
     */
    private ReviewComment findCommentById(Long reviewCommentSeq) {
        return reviewCommentRepository.findById(reviewCommentSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_REPLY));
    }

    /**
     * 파일 업로드 처리
     */
    private File uploadFileToS3(MultipartFile image) {
        String uploadedPath = s3ImageService.upload(image);
        log.info("File uploaded successfully: {}", uploadedPath);
        return new File(uploadedPath, image.getOriginalFilename());
    }

    /**
     * S3에서 파일 삭제
     */
    private void deleteFileFromS3(File file) {
        try {
            s3ImageService.deleteImageFromS3(file.getPath());
            log.info("File deleted successfully: {}", file.getPath());
        } catch (Exception e) {
            log.error("File deletion failed: {}", e.getMessage());
        }
    }
}
