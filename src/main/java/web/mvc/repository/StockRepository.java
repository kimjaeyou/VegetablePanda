package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import web.mvc.domain.Product;
import web.mvc.domain.Stock;
import web.mvc.dto.AllStockDTO;
import web.mvc.dto.GetProData;
import web.mvc.dto.StockInfoDTO;
import web.mvc.dto.StockUserSeqDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Query("select new web.mvc.dto.StockInfoDTO(s.stockSeq, s.content, s.count, s.color, s.product.productCategory.content, s.product.productSeq, s.product.productName, s.stockGrade.grade, s.stockOrganic.organicStatus, s.farmerUser.userSeq, s.farmerUser.userSeq, s.regDate, s.file.fileSeq, s.file.name, s.file.path) from Stock s left join s.file where s.farmerUser.userSeq = ?1")
    List<StockInfoDTO> findStockInfoById(long id);

    @Query("select s from Stock s where s.stockSeq=?1 and s.count>=?2")
    Stock checkCount(Long stockSeq,int count);

    @Query("select new web.mvc.dto.StockUserSeqDTO(s.farmerUser.userSeq) from Stock s where s.stockSeq=?1 and s.count<=0")
    StockUserSeqDTO checkCountZero(Long stockSeq);

    @Query("SELECT s FROM Stock s WHERE s.status in (1,2) AND s.regDate < :yesterday")
    List<Stock> findStocksBeforeYesterday(@Param("yesterday") LocalDateTime yesterday);

    Optional<Stock> findFirstByProductOrderByStockSeqDesc(Product product);

//    @Query(value = "select product_name, status ,count,stock_grade_seq,stock_organic_seq from stock s " +
//            "join product p on p.product_seq=s.product_seq " +
//            "where stock_seq=:stockSeq",nativeQuery = true)
//    GetProData findGetProData(@Param("stockSeq") Long stockSeq);
@Query(value = "select s.stock_seq as stockSeq, p.product_name as productName, s.status as status, s.count as count, " +
        "sg.grade as stockGradeSeq, so.organic_status as stockOrganicSeq, f.path as path " +
        "from stock s " +
        "join product p on p.product_seq = s.product_seq " +
        "join file f on f.file_seq = s.file_seq " +
        "join stock_grade sg on s.stock_grade_seq = sg.stock_grade_seq " +
        "join stock_organic so on s.stock_organic_seq = so.stock_organic " +
        "where s.stock_seq = :stockSeq", nativeQuery = true)
List<Object[]> findGetProData(@Param("stockSeq") Long stockSeq);


}
