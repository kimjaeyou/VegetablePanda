package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.UserBuyDetail;

import java.util.List;

public interface UserBuyDetailRepository extends JpaRepository<UserBuyDetail, Long> {

    UserBuyDetail findFirstByUserBuySeq(Long userBuySeq);

    @Query("select s.content from UserBuyDetail d join Stock s on s.stockSeq = d.stock.stockSeq where d.userBuy.buySeq = ?1")
    //@Query("select s.content from UserBuyDetail d left join fetch UserBuy b left join fetch Stock s where d.userBuySeq = ?1 order by d.userBuySeq limit 1")
    //select content from user_buy_detail d join user_buy b join stock s;
    List<String> findByBuySeq(Long userBuySeq);
}
