package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.UserBuy;

import java.util.List;

public interface UserBuyRepository extends JpaRepository<UserBuy,Integer> {

}
