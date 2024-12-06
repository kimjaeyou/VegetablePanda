
package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import web.mvc.domain.Shop;
import web.mvc.dto.SalesStatisticsDTO;
import web.mvc.dto.ShopListDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    @Query("SELECT new web.mvc.dto.ShopListDTO(s.shopSeq, s.stock.stockSeq, s.stock.content, " +
            "s.price, s.stock.count, " +
            "CAST(s.insertDate AS string), " +
            "s.stock.product.productName, " +
            "s.stock.stockGrade.grade, " +
            "s.stock.stockOrganic.organicStatus, " +
            "s.stock.file.path," +
            "s.stock.product.productCategory.content," +
            "s.stock.farmerUser.name)" +
            "FROM Shop s " +
            "LEFT JOIN s.stock.file " +
            "WHERE s.stock.status = 3 " +
            "ORDER BY s.insertDate DESC")
    List<ShopListDTO> findAllShopItems();

    @Query("SELECT new web.mvc.dto.ShopListDTO(s.shopSeq, s.stock.stockSeq, s.stock.content, " +
            "s.price, s.stock.count, " +
            "CAST(s.insertDate AS string), " +
            "s.stock.product.productName, " +
            "s.stock.stockGrade.grade, " +
            "s.stock.stockOrganic.organicStatus, " +
            "s.stock.file.path)" +
            "FROM Shop s " +
            "WHERE s.stock.status = 1  and s.stock.farmerUser.userSeq = ?1 " +
            "ORDER BY s.insertDate DESC")
    List<ShopListDTO> findByUserSeq(Long seq);

    @Query("SELECT new web.mvc.dto.ShopListDTO(s.shopSeq, s.stock.stockSeq, s.stock.content, " +
            "s.price, s.stock.count, " +
            "CAST(s.insertDate AS string), " +
            "s.stock.product.productName, " +
            "s.stock.stockGrade.grade, " +
            "s.stock.stockOrganic.organicStatus, " +
            "s.stock.file.path)" +
            "FROM Shop s " +
            "join Stock k on s.stock.stockSeq = k.stockSeq " +
            "join Product p on p.productSeq = k.product.productSeq " +
            "WHERE s.stock.status = 3 and s.stock.farmerUser.userSeq = ?1" +
            "ORDER BY s.insertDate DESC")
    List<ShopListDTO> findLikeItems(Long seq);

    // 일별 상품별 통계
    @Query(value =
            "SELECT DATE_FORMAT(ub.buy_date, '%Y-%m-%d') as period, " +
                    "MAX(p.product_name) as product_name, " +
                    "COUNT(DISTINCT ub.buy_seq) as total_sales, " +
                    "SUM(ubd.count) as total_quantity, " +
                    "SUM(ubd.price * ubd.count) as total_amount, " +
                    "'DAILY' as period_type " +
                    "FROM user_buy ub " +
                    "JOIN user_buy_detail ubd ON ub.buy_seq = ubd.user_buy_seq " +
                    "JOIN stock st ON ubd.stock_seq = st.stock_seq " +
                    "JOIN product p ON st.product_seq = p.product_seq " +
                    "WHERE ub.buy_date BETWEEN :startDate AND :endDate " +
                    "AND ub.state = 1 " +
                    "AND st.product_seq = (SELECT product_seq FROM stock WHERE stock_seq = :stockSeq) " +
                    "GROUP BY DATE_FORMAT(ub.buy_date, '%Y-%m-%d') " +
                    "ORDER BY period",
            nativeQuery = true)
    List<Object[]> findDailySalesStatistics(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("stockSeq") Long stockSeq
    );

    // 주별 상품별 통계
    @Query(value =
            "SELECT DATE_FORMAT(DATE_SUB(ub.buy_date, INTERVAL WEEKDAY(ub.buy_date) DAY), '%Y-%m-%d') as period, " +
                    "MAX(p.product_name) as product_name, " +
                    "COUNT(DISTINCT ub.buy_seq) as total_sales, " +
                    "SUM(ubd.count) as total_quantity, " +
                    "SUM(ubd.price * ubd.count) as total_amount, " +
                    "'WEEKLY' as period_type " +
                    "FROM user_buy ub " +
                    "JOIN user_buy_detail ubd ON ub.buy_seq = ubd.user_buy_seq " +
                    "JOIN stock st ON ubd.stock_seq = st.stock_seq " +
                    "JOIN product p ON st.product_seq = p.product_seq " +
                    "WHERE ub.buy_date BETWEEN :startDate AND :endDate " +
                    "AND ub.state = 1 " +
                    "AND st.product_seq = (SELECT product_seq FROM stock WHERE stock_seq = :stockSeq) " +
                    "GROUP BY DATE_FORMAT(DATE_SUB(ub.buy_date, INTERVAL WEEKDAY(ub.buy_date) DAY), '%Y-%m-%d') " +
                    "ORDER BY period",
            nativeQuery = true)
    List<Object[]> findWeeklySalesStatistics(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("stockSeq") Long stockSeq
    );

    // 월별 상품별 통계
    @Query(value =
            "SELECT DATE_FORMAT(ub.buy_date, '%Y-%m-01') as period, " +
                    "MAX(p.product_name) as product_name, " +
                    "COUNT(DISTINCT ub.buy_seq) as total_sales, " +
                    "SUM(ubd.count) as total_quantity, " +
                    "SUM(ubd.price * ubd.count) as total_amount, " +
                    "'MONTHLY' as period_type " +
                    "FROM user_buy ub " +
                    "JOIN user_buy_detail ubd ON ub.buy_seq = ubd.user_buy_seq " +
                    "JOIN stock st ON ubd.stock_seq = st.stock_seq " +
                    "JOIN product p ON st.product_seq = p.product_seq " +
                    "WHERE ub.buy_date BETWEEN :startDate AND :endDate " +
                    "AND ub.state = 1 " +
                    "AND st.product_seq = (SELECT product_seq FROM stock WHERE stock_seq = :stockSeq) " +
                    "GROUP BY DATE_FORMAT(ub.buy_date, '%Y-%m-01') " +
                    "ORDER BY period",
            nativeQuery = true)
    List<Object[]> findMonthlySalesStatistics(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("stockSeq") Long stockSeq
    );

    // 어제 최고가 조회
    @Query(value =
            "SELECT MAX(ubd.price) " +
                    "FROM user_buy ub " +
                    "JOIN user_buy_detail ubd ON ub.buy_seq = ubd.user_buy_seq " +
                    "JOIN stock st ON ubd.stock_seq = st.stock_seq " +
                    "WHERE DATE(ub.buy_date) = DATE_SUB(CURDATE(), INTERVAL 1 DAY) " +
                    "AND ub.state = 1 " +
                    "AND st.product_seq = (SELECT product_seq FROM stock WHERE stock_seq = :stockSeq)",
            nativeQuery = true)
    Integer findYesterdayMaxPrice(@Param("stockSeq") Long stockSeq);

    // 일주일 평균가 조회
    @Query(value =
            "SELECT ROUND(AVG(ubd.price)) " +
                    "FROM user_buy ub " +
                    "JOIN user_buy_detail ubd ON ub.buy_seq = ubd.user_buy_seq " +
                    "JOIN stock st ON ubd.stock_seq = st.stock_seq " +
                    "WHERE ub.buy_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) " +
                    "AND ub.buy_date < CURDATE() " +
                    "AND ub.state = 1 " +
                    "AND st.product_seq = (SELECT product_seq FROM stock WHERE stock_seq = :stockSeq)",
            nativeQuery = true)
    Integer findWeeklyAveragePrice(@Param("stockSeq") Long stockSeq);





    @Query("SELECT s FROM Shop s WHERE s.stock.stockSeq = :stockSeq")
    Optional<Shop> findByStockSeq(@Param("stockSeq") Long stockSeq);
}