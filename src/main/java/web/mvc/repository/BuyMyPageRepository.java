package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.dto.UserBuyDTO;
import web.mvc.domain.UserBuy;
import web.mvc.domain.UserBuyDetail;
import web.mvc.dto.UserBuyListForReivewDTO;

import java.util.List;

public interface BuyMyPageRepository extends JpaRepository<UserBuy, Long>, JpaSpecificationExecutor<UserBuyDetail> {
   /**
    * 주문내역 일반유저, 일반 상점 구매2, 결제대기5 승인대기 7 승인완료 9전체 조회
    */
   @Query("SELECT new web.mvc.dto.UserBuyListForReivewDTO(d.userBuySeq, d.stock.product.productName, d.count, d.price, d.userBuy.buyDate, d.userBuy.state,r.reviewSeq) " +
           "FROM UserBuyDetail d " +
           "JOIN Review r ON r.managementUser.userSeq = d.stock.farmerUser.userSeq " +
           "WHERE d.userBuy.managementUser.userSeq = ?1 AND d.userBuy.state IN (2,5,7,9) order by d.userBuy.buyDate DESC " )
   List<UserBuyListForReivewDTO> selectShopBuyByUserSeq(Long seq);
   /**
    * 주문내역 일반유저, 일반 경매 구매1, 승인대기 6 승인완료 10전체 조회
    */
   @Query("SELECT new web.mvc.dto.UserBuyListForReivewDTO(d.userBuySeq, d.stock.product.productName, d.count, d.price, d.userBuy.buyDate, d.userBuy.state,r.reviewSeq) " +
           "FROM UserBuyDetail d " +
           "JOIN Review r ON r.managementUser.userSeq = d.stock.farmerUser.userSeq " +
           "WHERE d.userBuy.managementUser.userSeq = ?1 AND d.userBuy.state IN (1,6,10) order by d.userBuy.buyDate DESC " )
   List<UserBuyListForReivewDTO> selectAuctionBuyByUserSeq(Long seq);


   @Query("SELECT new web.mvc.dto.UserBuyDTO(d.userBuySeq, z.content, d.count, d.price, d.userBuy.buyDate, b.state) " +
           "FROM UserBuyDetail d " +
           "JOIN UserBuy b ON b.buySeq = d.userBuySeq " +
           "JOIN Stock z ON z.stockSeq = d.stock.stockSeq " +
           "WHERE z.farmerUser.userSeq = ?1 AND b.state IN (1, 2, 4) order by d.userBuy.buyDate DESC")
   List<UserBuyDTO> selectAll(@Param("userSeq") Long seq);




   @Modifying(clearAutomatically = true)
   @Query("UPDATE UserBuy b " +
           "SET b.state = CASE " +
           "    WHEN b.state = 1 THEN 6 " +
           "    WHEN b.state = 2 THEN 7 " +
           "    WHEN b.state = 4 THEN 8 " +
           "    ELSE b.state END " +
           "WHERE b.buySeq = ?1")
   int update(Long buySeq);
}
