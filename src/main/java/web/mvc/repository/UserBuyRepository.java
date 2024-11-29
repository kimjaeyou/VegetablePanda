package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.UserBuy;
import web.mvc.domain.UserCharge;

import java.util.List;
import java.util.Optional;

public interface UserBuyRepository extends JpaRepository<UserBuy, Long> {

    @Query(value = "select b from UserBuy b join Payment p on b.payment.id = p.id where b.orderUid = ?1")
    //@Query(value = "select b from UserBuy b join Payment p on b.buySeq = p.userBuy.buySeq where p.userBuy.buySeq = ?1")
        // select * from finalproject.user_charge u join finalproject.payment p on u.payment_seq = p.payment_seq where u.order_uid = 'O202411222120442440';
    Optional<UserBuy> findOrderAndPayment(String orderUid);

    @Query(value = "select u from UserBuy u where u.orderUid=?1")
    List<UserBuy> findByOrderUid(String orderUid);
}
