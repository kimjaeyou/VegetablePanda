package web.mvc.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user_buy")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBuy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "buy_seq")
    private Long buySeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private ManagementUser managementUser;

    @Column(name = "buy_date")
    private LocalDateTime buyDate;

    @Column(name = "state")
    private Integer state;

    @Column(name = "total_price")
    private Integer totalPrice;

    @OneToMany(mappedBy = "userBuy")
    private List<Payment> payments;
}
