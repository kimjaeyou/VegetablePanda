package web.mvc.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "calc_point")
@Getter @Setter
public class CalcPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calc_point_seq")
    private Long calcPointSeq;

    @Column(name = "point")
    private Integer point;

    @Column(name = "point_to_cash")
    private Integer pointToCash;

    @Column(name = "trade_date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime tradeDate;

    @Column(name = "state")
    private Integer state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private ManagementUser managementUser;
}
