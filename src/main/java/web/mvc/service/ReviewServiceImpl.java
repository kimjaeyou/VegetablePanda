package web.mvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.ManagementUser;
import web.mvc.domain.Review;
import web.mvc.repository.ReviewRepository;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    //private final ReviewRepository reviewRepository;

    /**
     * 리뷰 페이지 조회
     * */
//    @Override
//    public List<Review> getReviews(ManagementUser managementUser, Review review) {
//
//        return reviewRepository.selectSeq();
//    }
}
