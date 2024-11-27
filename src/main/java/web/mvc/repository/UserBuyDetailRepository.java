package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import web.mvc.domain.UserBuyDetail;
import web.mvc.dto.DailyStatsDTO;
import web.mvc.dto.ProductStatisticsDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface UserBuyDetailRepository extends JpaRepository<UserBuyDetail, Long> {

    UserBuyDetail findFirstByUserBuySeq(Long userBuySeq);

    @Query("select s.content from UserBuyDetail d join Stock s on s.stockSeq = d.stock.stockSeq where d.userBuy.buySeq = ?1")
    //@Query("select s.content from UserBuyDetail d left join fetch UserBuy b left join fetch Stock s where d.userBuySeq = ?1 order by d.userBuySeq limit 1")
    //select content from user_buy_detail d join user_buy b join stock s;
    List<String> findByBuySeq(Long userBuySeq);

    @Query("SELECT NEW web.mvc.dto.ProductStatisticsDTO(" +
            "s.product.productName, " +
            "SUM(ubd.count), " +
            "SUM(ubd.price), " +
            "COUNT(DISTINCT ubd.userBuy)) " +
            "FROM UserBuyDetail ubd " +
            "JOIN ubd.stock s " +
            "JOIN ubd.userBuy ub " +
            "WHERE ub.buyDate BETWEEN :startDate AND :endDate " +
            "AND ub.state = 1 " +
            "GROUP BY s.product.productName " +
            "ORDER BY SUM(ubd.price) DESC")
    List<ProductStatisticsDTO> getProductSalesStatistics(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT " +
            "DATE_FORMAT(ub.buyDate, '%Y-%m-%d') as date, " +
            "SUM(ubd.count) as totalQuantity, " +
            "SUM(ubd.price) as totalAmount " +
            "FROM UserBuyDetail ubd " +
            "JOIN ubd.userBuy ub " +
            "WHERE ub.buyDate BETWEEN :startDate AND :endDate " +
            "AND ub.state = 1 " +
            "GROUP BY DATE_FORMAT(ub.buyDate, '%Y-%m-%d') " +
            "ORDER BY DATE_FORMAT(ub.buyDate, '%Y-%m-%d') ASC")
    List<Object[]> findDailyStats(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
