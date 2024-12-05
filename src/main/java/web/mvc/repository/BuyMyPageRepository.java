package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import web.mvc.dto.UserBuyDTO;
import web.mvc.domain.UserBuy;
import web.mvc.domain.UserBuyDetail;

import java.util.List;

public interface BuyMyPageRepository extends JpaRepository<UserBuy, Long>, JpaSpecificationExecutor<UserBuyDetail> {
   /**
    * 주문내역 전체 조회
    */
   @Query("SELECT new web.mvc.dto.UserBuyDTO(d.userBuySeq, z.content, d.count, d.price, d.userBuy.buyDate, b.state) " +
           "FROM UserBuyDetail d " +
           "JOIN UserBuy b ON b.buySeq = d.userBuySeq " +
           "JOIN Stock z ON z.stockSeq = d.stock.stockSeq " +
           "WHERE z.farmerUser.userSeq = ?1 AND b.state = 2 and b.state = 4 order by d.userBuy.buyDate DESC " )
   List<UserBuyDTO> select(@Param("userSeq") Long seq);


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
