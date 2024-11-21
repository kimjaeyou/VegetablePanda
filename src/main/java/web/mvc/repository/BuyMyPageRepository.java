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
     */
    @Query("select d.userBuySeq , d.count, d.price from UserBuyDetail d  left join UserBuy b on b.buySeq = d.userBuySeq where b.managementUser.userSeq = ?1")
    List<UserBuyDTO> selectAll (Long seq);
}
