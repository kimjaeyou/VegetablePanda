package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.CalcPoint;

import java.util.List;

public interface CalcPointRepository extends JpaRepository<CalcPoint, Long> {

    @Query("select new web.mvc.dto.CalcPoint(c.calcPointSeq, c.totalPoint, c.pointToCash, c.tradeDate, c.insertDate,c.state) from CalcPoint c where c.managementUser.userSeq = ?1")
    List<web.mvc.dto.CalcPoint> selectCalc(Long seq);


}