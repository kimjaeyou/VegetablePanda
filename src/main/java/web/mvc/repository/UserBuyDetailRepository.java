package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.mvc.domain.UserBuyDetail;

public interface UserBuyDetailRepository extends JpaRepository<UserBuyDetail, Long> {
}
