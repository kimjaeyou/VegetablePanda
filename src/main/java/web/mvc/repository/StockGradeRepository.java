package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.mvc.domain.StockGrade;

public interface StockGradeRepository extends JpaRepository<StockGrade, Long> {
}
