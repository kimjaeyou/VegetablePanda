package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import web.mvc.domain.Stock;
import web.mvc.dto.AllStockDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {

    @Query("select distinct s from Stock s left join fetch s.product left join fetch s.stockGrade left join fetch s.stockOrganic left join fetch s.farmerUser left join fetch s.userBuyDetails where s.farmerUser.userSeq = ?1")
    List<Stock> findStocksById(long farmerId);

    @Query("select new web.mvc.dto.AllStockDTO(s.content, s.count, s.color, s.product.productName ,s.stockGrade.grade, s.stockOrganic.organicStatus, s.farmerUser.name, s.farmerUser.farmerGrade.gradeContent, s.farmerUser.phone, s.file.path) from Stock s where s.farmerUser.userSeq = ?1 and s.status=1")
    AllStockDTO findAuctionStocksById(long farmerId);

    @Modifying
    @Query("update Stock s set s.count=s.count -?2 where s.stockSeq=?1 ")
    public void reduceCount(long stockSeq, int count);

    @Query("select s from Stock s where s.farmerUser.userSeq = :farmerSeq")
    List<Stock> findByFarmerUserSeq(Long farmerSeq);

    @Query("SELECT s FROM Stock s WHERE s.status = :status AND s.regDate > :regDate")
    List<Stock> findByStatusAndRegDateAfter(@Param("status") Integer status, @Param("regDate") LocalDateTime regDate);

    @Query("SELECT COUNT(s) > 0 FROM Stock s " +
            "WHERE s.farmerUser.userSeq = :farmerSeq " +
            "AND s.regDate BETWEEN :startDate AND :endDate")
    boolean existsByFarmerAndDateRange(
            @Param("farmerSeq") long farmerSeq,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
