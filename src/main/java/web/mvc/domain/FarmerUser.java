package web.mvc.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "farmer_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FarmerUser {
    @Id
    @Column(name = "user_seq")
    private Long userSeq;

    @Column(name = "farmer_id", nullable = false, length = 60, unique = true)
    private String farmerId;

    @Column(name = "pw", nullable = false, length = 60)
    private String pw;

    @Column(name = "name", nullable = false, length = 60)
    private String name;

    @Column(name = "address", nullable = false, length = 300)
    private String address;

    @Column(name = "phone", nullable = false, length = 50)
    private String phone;

    @Column(name = "code", length = 60)
    private String code;

    @Column(name = "email", nullable = false , length = 50)
    private String email;

    @Column(name = "state", nullable = false, length = 10)
    private Integer state;

    @Column(name = "reg_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime regDate;

    @Column(name = "acount", length = 60)
    private String account;

    private String role;

    @ManyToOne(fetch = FetchType.LAZY)
    private FarmerGrade farmerGrade;

    @OneToMany(mappedBy = "farmerUser",fetch = FetchType.LAZY)
    private List<Likes> likes;


    public FarmerUser(Long user_seq,String farmerId, String pw,String name,
                      String address,String code,String account,String phone,
                      String email,int state,String role) {
        this.userSeq = user_seq;
        this.farmerId = farmerId;
        this.name = name;
        this.pw = pw;
        this.address = address;
        this.code = code;
        this.account = account;
        this.phone = phone;
        this.email = email;
        this.state = state;
        this.role = role;
        this.farmerGrade = farmerGrade;
        this.regDate = LocalDateTime.now();
    }
    public FarmerUser (long farmerSeq){
        this.userSeq = farmerSeq;
    }
}