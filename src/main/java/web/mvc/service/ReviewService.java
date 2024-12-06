package web.mvc.service;

import org.springframework.http.ResponseEntity;
import web.mvc.domain.ManagementUser;
import web.mvc.domain.Review;
import web.mvc.dto.ReviewDTO;
import web.mvc.dto.ReviewDTO2;

import java.util.List;
import java.util.Optional;

public interface ReviewService {


    Optional<Review> findByManagementUserId(Long managementUserId);

    ReviewDTO2 convertToDTO(Review review);

}
