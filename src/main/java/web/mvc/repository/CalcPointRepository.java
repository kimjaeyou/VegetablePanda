package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.CalcPoint;
import web.mvc.domain.UserBuy;
import web.mvc.dto.UserBuyDTO;

public interface CalcPointRepository extends JpaRepository<CalcPoint, Long> {
}
