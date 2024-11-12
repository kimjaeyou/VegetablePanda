package web.mvc.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "delivery")
@Data
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_seq")
    private Integer deliverySeq;

    @Column(name = "locate", nullable = false, length = 100)
    private String locate;

    @Column(name = "destination", nullable = false, length = 100)
    private String destination;

    @OneToOne
    @JoinColumn(name = "user_buy_buy_seq", nullable = false)
    private UserBuy userBuy;
}
