package web.mvc.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_buy_detail")
@Getter
@Setter

public class UserBuyDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_buy_detail_seq")
    private Integer userBuySeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_buy_seq", nullable = false)
    private UserBuy userBuy;

    @Column(name = "content", length = 60)
    private String content;

    @Column(name = "price")
    private Integer price;

    @Column(name = "count")
    private Integer count;

    // 추가
    @Column(name = "discount")
    private Integer discount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_seq", nullable = false)
    private Stock stock;
}
