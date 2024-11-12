package web.mvc.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "auction")
@Data
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auction_seq")
    private Integer auctionSeq;

    @Column(name = "count", nullable = false)
    private Integer count;

    @Column(name = "close_time", nullable = false)
    private LocalDateTime closeTime;

    @Column(name = "bid_price")
    private Integer bidPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_seq", nullable = false)
    private Stock stock;
}
