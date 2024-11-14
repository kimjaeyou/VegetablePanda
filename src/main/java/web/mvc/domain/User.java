package web.mvc.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Data
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    private Integer userSeq;

    @Column(name = "user_id", nullable = false, length = 60, unique = true)
    private String userId;

    @Column(name = "pw", nullable = false, length = 60)
    private String pw;

    @Column(name = "address", nullable = false, length = 300)
    private String address;

    @Column(name = "phone", nullable = false, length = 50)
    private String phone;

    @Column(name = "reg_date", nullable = false)
    private LocalDateTime regDate;

    @Column(name = "up_date", nullable = false)
    private LocalDateTime upDate;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_seq")
    private ManagementUser managementUser;
}
