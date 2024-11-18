package web.mvc.repository;

import com.siot.IamportRestClient.response.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.UserCharge;

public interface UserChargeRepository extends JpaRepository<UserCharge, Integer> {

    @Query(value = "select u from UserCharge u order by u.userChargeSeq desc limit 1")
    UserCharge getLastCharge();
}
