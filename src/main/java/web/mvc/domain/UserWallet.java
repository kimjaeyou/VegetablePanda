package web.mvc.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_wallet")
@Getter
@Setter
public class UserWallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_wallet_seq")
    private Integer userWalletSeq;

    @Column(name = "point", nullable = false)
    private Integer point;

    @OneToOne
    @JoinColumn(name = "user_seq", nullable = false)
    private ManagementUser managementUser;
}
