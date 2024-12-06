package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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


   //판매내역
   @Query("SELECT new web.mvc.dto.UserBuyDTO(d.userBuySeq, z.content, d.count, d.price, d.userBuy.buyDate, b.state) " +
           "FROM UserBuyDetail d " +
           "JOIN UserBuy b ON b.buySeq = d.userBuySeq " +
           "JOIN Stock z ON z.stockSeq = d.stock.stockSeq " +
           "WHERE z.farmerUser.userSeq = ?1 AND b.state = 2 and b.state = 6 and b.state = 7 order by d.userBuy.buyDate DESC " )
   List<UserBuyDTO> selectAll(@Param("userSeq") Long seq);

   // 상태값 판매상태값 바꾸기
   @Modifying
   @Query("update UserBuy b set b.state = ?1 where b.managementUser.userSeq = ?2 and b.buySeq = ?3 ")
   int update (int state, Long userSeq, Long buySeq);



}
 