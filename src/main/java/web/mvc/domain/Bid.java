package web.mvc.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "bid")
@Getter
@Setter
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bid_seq")
    private Integer bidSeq;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "insertDate")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime insertDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_auction_seq", nullable = false)
    private Auction auction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "management_user_user_seq", nullable = false)
    private ManagementUser managementUser;
}
