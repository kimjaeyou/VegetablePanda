package web.mvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.ManagementUser;
import web.mvc.domain.Review;
import web.mvc.dto.ReviewDTO;
import web.mvc.dto.ReviewDTO2;
import web.mvc.repository.ReviewRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public Optional<Review> findByManagementUserId(Long managementUserId) {
        return reviewRepository.findByManagementUser_UserSeq(managementUserId);
    }

    @Override
    public ReviewDTO2 convertToDTO(Review review) {
        return ReviewDTO2.builder()
                .reviewSeq(review.getReviewSeq())
                .visitNum(review.getVisitNum())
                .intro(review.getIntro())
                .managementUserId(review.getManagementUser().getUserSeq())
                .lastCommentDate(review.getReviewComments().stream()
                        .map(c -> c.getDate())
                        .max(LocalDateTime::compareTo)
                        .orElse(null))
                .build();
    }

}
