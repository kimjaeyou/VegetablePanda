package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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
           "WHERE b.managementUser.userSeq = ?1 AND b.state = ?2")
   List<UserBuyDTO> selectAll(@Param("userSeq") Long seq, Integer state);

}
