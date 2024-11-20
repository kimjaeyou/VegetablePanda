package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.mvc.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {


}
