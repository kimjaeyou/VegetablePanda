package web.mvc.domain;

import jakarta.persistence.*;
import lombok.*;
import web.mvc.payment.PaymentStatus;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE ,generator = "payment_seq")
    @Column(name="payment_seq")
    private long id;

    private long price;
    private PaymentStatus status;
    private String paymentUid;

    @Builder
    public Payment(long price, PaymentStatus status){
        this.price = price;
        this.status = status;
    }

    public void changePaymentBySuccess(PaymentStatus status, String paymentUid){
        this.status = status;
        this.paymentUid = paymentUid;
    }
}
