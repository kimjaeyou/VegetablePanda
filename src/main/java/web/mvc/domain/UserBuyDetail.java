package web.mvc.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_buy_detail")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class UserBuyDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_buy_detail_seq")
    private Long userBuySeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_buy_seq", nullable = false)
    private UserBuy userBuy;

    @Column(name = "price")
    private Integer price;

    @Column(name = "count")
    private Integer count;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_seq", nullable = false)
    private Stock stock;

    @OneToOne(mappedBy = "userBuyDetail")
    private ReviewComment reviewComment;

}
