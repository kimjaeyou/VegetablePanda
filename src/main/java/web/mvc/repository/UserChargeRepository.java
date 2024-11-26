package web.mvc.repository;

import com.siot.IamportRestClient.response.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.UserCharge;

import java.util.List;
import java.util.Optional;

public interface UserChargeRepository extends JpaRepository<UserCharge, Long> {

    @Query(value = "select u from UserCharge u order by u.userChargeSeq desc limit 1")
    UserCharge getLastCharge();

    boolean existsByOrderUid(String orderUid);

    @Query(value = "select c from UserCharge c left join fetch c.managementUser u left join fetch c.payment p")
    Optional<UserCharge> findUserChargeAndPaymentAndManagementUser(String orderUid);

    @Query(value = "select u from UserCharge u where u.orderUid=?1")
    List<UserCharge> findByOrderUid(String orderUid);

    @Query(value = "select u from UserCharge u join Payment p on u.payment.id = p.id where u.orderUid = ?1")
    // select * from finalproject.user_charge u join finalproject.payment p on u.payment_seq = p.payment_seq where u.order_uid = 'O202411222120442440';
    Optional<UserCharge> findOrderAndPayment(String orderUid);

}
