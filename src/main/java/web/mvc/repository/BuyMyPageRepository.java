package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import web.mvc.dto.UserBuyDTO;
import web.mvc.domain.UserBuy;
import web.mvc.domain.UserBuyDetail;

import java.util.List;

public interface BuyMyPageRepository extends JpaRepository<UserBuy, Long>, JpaSpecificationExecutor<UserBuyDetail> {
   /**
    * 주문내역 전체 조회
    * 주문 조회 내역은 시퀀스로 찾아온다.
    * 시퀀스값이 일치한 데이터들만 List에 담아서 리턴
    * 가져오는 값 : 주문번호 , 상품명, 수량, 가격, 구매날짜
    */
    @Query("select d.userBuySeq , z.content , d.count, d.price, d.userBuy.buyDate , z.farmerUser.name from UserBuyDetail d  join UserBuy b on b.buySeq = d.userBuySeq join Stock z on z.stockSeq = d.stock.stockSeq where b.managementUser.userSeq = ?1 and b.state = 1")
    List<UserBuyDTO> selectAll (Long seq);

    @Query("select d.userBuySeq , z.content , d.count, d.price, d.userBuy.buyDate, b.managementUser.id from UserBuyDetail d  join UserBuy b on b.buySeq = d.userBuySeq join Stock z on z.stockSeq = d.stock.stockSeq where b.managementUser.userSeq = ?1 and b.state = 3")
    List<UserBuyDTO> saleSelectAll (Long seq);

}
