package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.UserBuy;
import web.mvc.dto.AdjustmentDTO;
import web.mvc.dto.BidAuctionDTO;
import web.mvc.dto.UserBuyListByStockDTO;

import java.util.List;
import web.mvc.domain.UserCharge;

import java.util.List;
import java.util.Optional;

public interface UserBuyRepository extends JpaRepository<UserBuy, Long> {

    @Query("select new web.mvc.dto.UserBuyListByStockDTO(d.price, d.count, b.buyDate, d.stock.product.productName, d.stock.product.productCategory.content, d.stock.stockOrganic.organicStatus, d.stock.stockGrade.grade) from UserBuyDetail d join UserBuy b on b.buySeq = d.userBuy.buySeq where d.stock.stockSeq = ?1 order by b.buyDate DESC ")
    public List<UserBuyListByStockDTO> findByStockSeq(Long stockSeq);

    @Query(value = "select b from UserBuy b join Payment p on b.payment.id = p.id where b.orderUid = ?1")
    //@Query(value = "select b from UserBuy b join Payment p on b.buySeq = p.userBuy.buySeq where p.userBuy.buySeq = ?1")
        // select * from finalproject.user_charge u join finalproject.payment p on u.payment_seq = p.payment_seq where u.order_uid = 'O202411222120442440';
    Optional<UserBuy> findOrderAndPayment(String orderUid);

    @Query(value = "select u from UserBuy u where u.orderUid=?1")
    List<UserBuy> findByOrderUid(String orderUid);

    @Query("SELECT new web.mvc.dto.AdjustmentDTO(b.buySeq, f.name, p.productName, b.totalPrice, b.state, b.buyDate) " +
            "FROM UserBuy b " +
            "JOIN UserBuyDetail d ON d.userBuy.buySeq = b.buySeq " +
            "JOIN Stock s ON s.stockSeq = d.stock.stockSeq " +
            "JOIN FarmerUser f ON f.userSeq = s.farmerUser.userSeq " +
            "JOIN Product p ON p.productSeq = s.product.productSeq " +
            "WHERE b.state = 6 " +
            "ORDER BY b.buyDate DESC")
    List<AdjustmentDTO> findPendingSettlements();

    @Query("SELECT distinct new web.mvc.dto.BidAuctionDTO(u.buySeq, p.productName, ubd.count, u.totalPrice, u.buyDate, s.farmerUser.name, u.state) " +
            "FROM UserBuy u " +
            "Join UserBuyDetail ubd on u.buySeq = ubd.userBuy.buySeq " +
            "JOIN Stock s ON s.stockSeq = ubd.stock.stockSeq " +
            "Join Product p on p.productSeq = s.product.productSeq " +
            "Join FarmerUser f on f.userSeq = s.farmerUser.userSeq " +
            "WHERE u.managementUser.userSeq = ?1 " +
            "AND u.state = 1")
    List<BidAuctionDTO> successfulBidList(Long seq);
}
