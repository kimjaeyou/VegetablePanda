package web.mvc.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class UserCharge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_charge_seq")
    private Integer userChargeSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private ManagementUser user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "charge_date", length = 60)
    private String chargeDate;

    @Column(name = "price")
    private Integer price;
}
