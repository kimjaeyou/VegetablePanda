package web.mvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import web.mvc.domain.*;
import web.mvc.dto.ReviewCommentDTO;
import web.mvc.exception.DMLException;
import web.mvc.exception.ErrorCode;
import web.mvc.repository.ManagementRepository;
import web.mvc.repository.ReviewCommentRepository;
import web.mvc.repository.ReviewRepository;
import web.mvc.repository.UserBuyDetailRepository;

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
    private final ManagementRepository managementRepository;
    private final UserBuyDetailRepository userBuyDetailRepository;

    /**
     * 댓글 저장
     */
    @Override
    public ReviewCommentDTO reviewCommentSave(Long reviewSeq, ReviewCommentDTO reviewCommentDTO, MultipartFile file) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        ManagementUser managementUser = managementRepository.findByUserId(userId);
        if (managementUser == null) throw new DMLException(ErrorCode.NOTFOUND_USER);

        List<UserBuyDetail> userBuyDetails = userBuyDetailRepository.findByUserSeq(managementUser.getUserSeq());
        if (userBuyDetails.isEmpty()) throw new DMLException(ErrorCode.ORDER_NOTFOUND);

        UserBuyDetail userBuyDetail = userBuyDetails.get(0);
        Review review = findReviewById(reviewSeq);

        ReviewComment reviewComment = reviewCommentDTO.toEntity();
        reviewComment.setReview(review);
        reviewComment.setManagementUser(managementUser);
        reviewComment.setUserBuyDetail(userBuyDetail);

        if (file != null && !file.isEmpty()) {
            reviewComment.setFile(uploadFileToS3(file));
        }

        return ReviewCommentDTO.fromEntity(reviewCommentRepository.save(reviewComment));
    }

    @Override
    public ReviewCommentDTO reviewCommentUpdate(Long reviewSeq, Long reviewCommentSeq, ReviewCommentDTO reviewCommentDTO, MultipartFile file, boolean deleteFile) {

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        ManagementUser managementUser = managementRepository.findByUserId(userId);
        if (managementUser == null) throw new DMLException(ErrorCode.NOTFOUND_USER);

        ReviewComment existingComment = findCommentById(reviewCommentSeq);
        Review review = findReviewById(reviewSeq);

        if (!existingComment.getManagementUser().getId().equals(userId)) {
            throw new DMLException(ErrorCode.NOTFOUND_USER);
        }

        //파일 삭제
        if (deleteFile && existingComment.getFile() != null) {
            deleteFileFromS3(existingComment.getFile());
            existingComment.setFile(null);
        }

        // 새로운 파일 업로드 처리
        if (file != null && !file.isEmpty()) {
            // 기존 파일 삭제
            if (existingComment.getFile() != null) {
                deleteFileFromS3(existingComment.getFile());
            }
            // 새 파일 설정
            existingComment.setFile(uploadFileToS3(file));
        }

        existingComment.setContent(reviewCommentDTO.getContent());
        existingComment.setScore(reviewCommentDTO.getScore());
        existingComment.setReview(review);

        return ReviewCommentDTO.fromEntity(reviewCommentRepository.save(existingComment));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewCommentDTO> reviewCommentFindAllByUserId(Long userId) {
        log.info("사용자 ID로 댓글 조회: userId={}", userId);

        List<ReviewComment> comments = reviewCommentRepository.findAllByManagementUser_UserSeq(userId);

        log.info("조회된 댓글 수: {}", comments.size());

        return comments.stream()
                .map(ReviewCommentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 특정 리뷰에 대한 댓글 전체 조회
     */
    @Override
    @Transactional(readOnly = true)
    public List<ReviewCommentDTO> reviewCommentFindAllByReviewId(Long reviewSeq) {
        List<ReviewComment> comments = reviewCommentRepository.findAllByReview_ReviewSeq(reviewSeq);
        if (comments.isEmpty()) {
            throw new DMLException(ErrorCode.NOTFOUND_REPLY);
        }
        return comments.stream()
                .map(ReviewCommentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 댓글 삭제
     */
    @Override
    public void reviewCommentDelete(Long reviewCommentSeq) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        ReviewComment comment = findCommentById(reviewCommentSeq);

        // 댓글 작성자 확인
        if (!comment.getManagementUser().getId().equals(userId)) {
            throw new DMLException(ErrorCode.NOTFOUND_USER);
        }

        // S3에서 파일 삭제
        if (comment.getFile() != null) {
            deleteFileFromS3(comment.getFile());
        }

        reviewCommentRepository.deleteById(reviewCommentSeq);
    }

    /**
     * 리뷰 ID로 리뷰 조회
     */
    private Review findReviewById(Long reviewSeq) {
        return reviewRepository.findById(reviewSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));
    }

    /**
     * 댓글 ID로 댓글 조회
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
        return new File(uploadedPath, image.getOriginalFilename());
    }

    /**
     * S3에서 파일 삭제
     */
    private void deleteFileFromS3(File file) {
        s3ImageService.deleteImageFromS3(file.getPath());
    }
}
