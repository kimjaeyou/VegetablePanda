package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.mvc.domain.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
}
