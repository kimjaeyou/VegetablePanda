package web.mvc.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCharge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_charge_seq")
    private Integer userChargeSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private ManagementUser managementUser;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "charge_date", length = 60)
    private String chargeDate;

    @Column(name = "price")
    private long price;

    @Column(name = "order_uid", unique = true)
    private String orderUid;
}
