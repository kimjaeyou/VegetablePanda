package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.Stock;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Integer> {

    @Query("select distinct s from Stock s left join fetch s.product left join fetch s.stockGrade left join fetch s.stockOrganic left join fetch s.farmerUser left join fetch s.userBuys where s.farmerUser.userSeq = ?1")
    List<Stock> findStocksById(long farmerId);
}
