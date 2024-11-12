package web.mvc.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_buy_detail")
@Data
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
}
