package web.mvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import web.mvc.domain.*;
import web.mvc.dto.FileDTO;
import web.mvc.dto.ReviewCommentDTO;
import web.mvc.dto.ReviewCommentDetailDTO;
import web.mvc.dto.ReviewCommentStatisticsDTO;
import web.mvc.exception.DMLException;
import web.mvc.exception.ErrorCode;
import web.mvc.repository.ManagementRepository;
import web.mvc.repository.ReviewCommentRepository;
import web.mvc.repository.ReviewRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReviewCommentServiceImpl implements ReviewCommentService {

    private final ReviewCommentRepository reviewCommentRepository;
    private final ReviewRepository reviewRepository;
    private final S3ImageService s3ImageService;
    private final ManagementRepository managementRepository;
    private final FileService fileService;

    /**
     * 댓글 저장
     */
    @Override
    public ReviewCommentDTO reviewCommentSave(ReviewComment reviewComment) {
        ReviewComment savedComment = reviewCommentRepository.save(reviewComment);
        ReviewCommentDTO reviewCommentDTO = ReviewCommentDTO.builder()
                .reviewCommentSeq(savedComment.getReviewCommentSeq())
                .score(savedComment.getScore())
                .content(savedComment.getContent())
                .file(new FileDTO(savedComment.getFile()))
                .regDate(savedComment.getDate())
                .reviewSeq(savedComment.getReview().getReviewSeq())
                .userBuyDetailSeq(savedComment.getUserBuyDetail().getUserBuySeq())
                .userSeq(savedComment.getManagementUser().getUserSeq())
                .build();
        return reviewCommentDTO;
    }

    @Override
    public Optional<Review> findByFarmerUserId(Long userSeq) {
        return reviewRepository.findByFarmerUserId(userSeq);
    }


    //댓글 업뎃
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
    //내가 쓴 리뷰 목록 보기
    @Override
    @Transactional(readOnly = true)
    public List<ReviewCommentDetailDTO> reviewCommentFindAllByUserId(Long userId) {
        log.info("사용자 ID로 댓글 조회: userId={}", userId);

        List<ReviewCommentDetailDTO> comments = reviewCommentRepository.findAllByManagementUser_UserSeq(userId);

        log.info("조회된 댓글 수: {}", comments.size());

        return comments;
    }

    /**
     * 자신이 단 리뷰 조회
     */
    @Override
    @Transactional(readOnly = true)
    public ReviewCommentDetailDTO reviewCommentFindAllByReviewId(Long reviewSeq) {
        ReviewCommentDetailDTO comments = reviewCommentRepository.findByReviewUser(reviewSeq);

        if (comments==null) {
            throw new DMLException(ErrorCode.NOTFOUND_REPLY);
        }
        return comments;
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
     * 현재 로그인한 사용자 ID 가져오기
     */
    private String getCurrentUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * 파일 업로드 처리
     */
    private File uploadFileToS3(MultipartFile image) {
        String uploadedPath = s3ImageService.upload(image); // S3에 업로드
        return File.builder()
                .path(uploadedPath) // S3 URL 저장
                .name(image.getOriginalFilename()) // 원본 파일 이름 저장
                .build();
    }

    /**
     * S3에서 파일 삭제
     */
    private void deleteFileFromS3(File file) {
        s3ImageService.deleteImageFromS3(file.getPath());
    }

    /**
     * 하나의 재고에 대한 리뷰 목록 가져오기
     */
    @Override
    public List<ReviewCommentDetailDTO> getStockReviewList (Long stockSeq) {
        return reviewCommentRepository.findByStockReview(stockSeq);
    }

    /**
     * 하나의 재고에 대한 리뷰 개수와 평균점수 가져오기
     */
    public ReviewCommentStatisticsDTO getStockReviewStatistics (Long stockSeq) {

        return reviewCommentRepository.findStockReviewStatistics(stockSeq);
    }

}
