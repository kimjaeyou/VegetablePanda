package web.mvc.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @Column(name = "pw", nullable = false, length = 100)
    private String pw;

    @Column(name = "com_name", nullable = false, length = 60)
    private String comName;

    @Column(name = "owner_name", nullable = false, length = 60)
    private String ownerName;

    @Column(name = "phone", nullable = false, length = 45)
    private String phone;

    @Column(name = "address", nullable = false, length = 100)
    private String address;

    @Column(name = "code", nullable = false, length = 100)
    private String code;

    @Column(name = "state", nullable = false, length = 10)
    private Integer state;

    @Column(name = "email", nullable = false, length = 40)
    private String email;

    @Column(name="regDate")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime regDate;

    private String role;

    public CompanyUser(Long user_seq,String userId,String comName,String ownerName, String pw,
                String address,String phone,String code,
                String email,int state,String role) {
        this.userSeq = user_seq;
        this.companyId = userId;
        this.comName = comName;
        this.ownerName = ownerName;
        this.pw = pw;
        this.address = address;
        this.phone = phone;
        this.code = code;
        this.email = email;
        this.state = state;
        this.role = role;
    }

}
