package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.mvc.domain.StockOrganic;

public interface StockOrganicRepository extends JpaRepository<StockOrganic, Integer> {
}
