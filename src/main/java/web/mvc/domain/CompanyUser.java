package web.mvc.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "company_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyUser {
    @Id
    @Column(name = "user_seq")
    private Long userSeq;

    @Column(name = "company_id", nullable = false, length = 45)
    private String companyId;

    @Column(name = "pw", nullable = false, length = 45)
    private String pw;

    @Column(name = "com_name", nullable = false, length = 60)
    private String comName;

    @Column(name = "owner_name", nullable = false, length = 60)
    private String ownerName;

    @Column(name = "phone", nullable = false, length = 45)
    private String phone;

    @Column(name = "adress", nullable = false, length = 100)
    private String adress;

    @Column(name = "code", nullable = false, length = 100)
    private String code;

    @Column(name = "state", nullable = false, length = 10)
    private Integer state;

    private String role;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name = "user_seq")
    private ManagementUser managementUser;
}
