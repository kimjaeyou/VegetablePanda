package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import web.mvc.domain.UserBuyDetail;
import web.mvc.dto.ProductStatisticsDTO;
import web.mvc.dto.UserBuyDetailGetAvgPriceDTO;
import web.mvc.dto.UserBuyDetailInfoDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserBuyDetailRepository extends JpaRepository<UserBuyDetail, Long> {

    UserBuyDetail findFirstByUserBuySeq(Long userBuySeq);

    @Query("select s.product.productName from UserBuyDetail d join Stock s on s.stockSeq = d.stock.stockSeq where d.userBuy.buySeq = ?1")
    List<String> findproductNameByBuySeq(Long userBuySeq);



    @Query("SELECT NEW web.mvc.dto.ProductStatisticsDTO(" +
            "p.productName, " +
            "SUM(ubd.count), " +
            "SUM(ubd.price), " +
            "COUNT(DISTINCT ubd.userBuy)) " +
            "FROM UserBuyDetail ubd " +
            "JOIN ubd.stock s " +
            "JOIN s.product p " +
            "JOIN ubd.userBuy ub " +
            "WHERE ub.buyDate BETWEEN :startDate AND :endDate " +
            "AND ub.state IN (0, 1, 2, 4, 9, 10) " +
            "GROUP BY p.productName " +
            "ORDER BY SUM(ubd.price) DESC")
    List<ProductStatisticsDTO> findProductStats(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query(value =
            "SELECT " +
                    "   DATE_FORMAT(ub.buy_date, '%Y-%m-%d') as date, " +
                    "   CAST(COALESCE(SUM(ubd.count), 0) as SIGNED) as total_quantity, " +
                    "   CAST(COALESCE(SUM(ub.total_price), 0) as SIGNED) as total_amount " +
                    "FROM user_buy ub " +
                    "LEFT JOIN user_buy_detail ubd ON ubd.user_buy_seq = ub.buy_seq " +
                    "WHERE ub.buy_date BETWEEN :startDate AND :endDate " +
                    "AND ub.state IN (0, 1, 2, 4, 9, 10) " +
                    "GROUP BY DATE_FORMAT(ub.buy_date, '%Y-%m-%d') " +
                    "ORDER BY date ASC",
            nativeQuery = true)
    List<Object[]> findDailyStats(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate

    );

    @Query("SELECT ubd FROM UserBuyDetail ubd " +
            "JOIN ubd.userBuy ub " +
            "JOIN ub.managementUser mu " +
            "WHERE mu.userSeq = :userSeq")
    List<UserBuyDetail> findByUserSeq(@Param("userSeq") Long userSeq);

    @Query("Select new web.mvc.dto.UserBuyDetailInfoDTO(s.product.productName, ubd.count, ubd.price, s.file.path, s.stockSeq) " +
            "from UserBuyDetail ubd join Stock s on s.stockSeq = ubd.stock.stockSeq where s.stockSeq IN :stockSeqs and ubd.userBuy.buySeq = :userBuySeq")
    List<UserBuyDetailInfoDTO> findInfoesByStockSeq(@Param("stockSeqs") List<Long> stockSeqs, Long userBuySeq);


    @Query("select new web.mvc.dto.UserBuyDetailGetAvgPriceDTO(avg(u.price)) from UserBuyDetail u where u.stock.stockSeq=?1")
    UserBuyDetailGetAvgPriceDTO getAvgPrice(Long stockSeq);

    Optional<UserBuyDetail> findFirstByUserBuy_ManagementUser_UserSeqOrderByUserBuy_BuyDateDesc(Long userSeq);

}
