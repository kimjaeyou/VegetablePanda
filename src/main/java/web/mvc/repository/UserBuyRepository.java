package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.mvc.domain.UserBuy;

public interface UserBuyRepository extends JpaRepository<UserBuy,Integer> {
}
