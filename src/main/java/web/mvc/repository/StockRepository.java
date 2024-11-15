package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.Stock;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Integer> {

    @Query("select s from Stock s where s.farmerUser.userSeq = ?1")
    List<Stock> findStocksById(long farmerId);
}
