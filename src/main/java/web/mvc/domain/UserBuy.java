package web.mvc.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user_buy")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBuy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "buy_seq")
    private Integer buySeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private User user;

    @Column(name = "buy_date")
    private LocalDateTime buyDate;

    @Column(name = "state")
    private Integer state;

    @Column(name = "stock_price")
    private Integer stockPrice;

    @Column(name = "stock_discount")
    private Integer stockDiscount;

    @OneToMany(mappedBy = "userBuy", fetch = FetchType.LAZY)
    private List<UserBuyDetail> userBuyDetails;
}
