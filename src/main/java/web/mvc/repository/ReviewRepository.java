package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select r from Review r where r.managementUser.userSeq = ?1")
    int selectSeq(int seq);
}
