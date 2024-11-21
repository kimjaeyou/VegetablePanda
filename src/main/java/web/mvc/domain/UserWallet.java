package web.mvc.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_wallet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserWallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_wallet_seq")
    private Long userWalletSeq;

    @Column(name = "point", nullable = false)
    private Integer point;

    @OneToOne
    @JoinColumn(name = "user_seq", nullable = false)
    private ManagementUser managementUser;

    public UserWallet(int point, long managementUserseq) {
        this.point=point;
        this.managementUser=new ManagementUser(managementUserseq);
    }
}