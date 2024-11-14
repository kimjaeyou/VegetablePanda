package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.mvc.domain.Product;
import web.mvc.domain.Stock;

public interface StockRepository extends JpaRepository<Stock, Integer> {
}
