package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.Payment;
import web.mvc.domain.UserBuy;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Payment findByPaymentUid(String paymentUid);

    @Query(value = "select p from Payment p join UserBuy b on b.payment.id = p.id where b.orderUid = ?1")
    Optional<Payment> findOrderAndPayment(String orderUid);
}
