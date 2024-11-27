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

    @Query("select s.content from UserBuyDetail d left join fetch UserBuy b left join fetch Stock s where d.userBuySeq = ?1 order by d.userBuySeq limit 1")
    //select content from user_buy_detail d join user_buy b join stock s;
    String findByBuySeq(Long userBuySeq);
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

    @Query(value =
            "SELECT " +
                    "   DATE_FORMAT(ub.buy_date, '%Y-%m-%d') as date, " +
                    "   CAST(COALESCE(SUM(ubd.count), 0) as SIGNED) as total_quantity, " +
                    "   CAST(COALESCE(SUM(ub.total_price), 0) as SIGNED) as total_amount " +
                    "FROM user_buy ub " +
                    "LEFT JOIN user_buy_detail ubd ON ubd.user_buy_seq = ub.buy_seq " +
                    "WHERE ub.buy_date BETWEEN :startDate AND :endDate " +
                    "AND ub.state = 1 " +
                    "GROUP BY DATE_FORMAT(ub.buy_date, '%Y-%m-%d') " +
                    "ORDER BY date ASC",
            nativeQuery = true)
    List<Object[]> findDailyStats(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
