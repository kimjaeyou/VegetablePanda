package web.mvc.repository;

import com.siot.IamportRestClient.response.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.UserCharge;

import java.util.Optional;

public interface UserChargeRepository extends JpaRepository<UserCharge, Integer> {

    @Query(value = "select u from UserCharge u order by u.userChargeSeq desc limit 1")
    UserCharge getLastCharge();

    boolean existsByOrderUid(String orderUid);

    @Query(value = "select c from UserCharge c left join fetch c.managementUser u left join fetch c.payment p")
    Optional<UserCharge> findUserChargeAndPaymentAndManagementUser(String orderUid);
}
