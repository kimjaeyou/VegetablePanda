package web.mvc.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "farmer_user")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FarmerUser {
    @Id
    @Column(name = "user_seq")
    private long userSeq;

    @OneToMany(mappedBy = "farmerUser",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Stock> stockList;

    @Column(name = "farmer_id", nullable = false, length = 60, unique = true)
    private String farmerId;

    @Column(name = "pw", nullable = false, length = 60)
    private String pw;

    @Column(name= "name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false, length = 300)
    private String address;

    @Column(name = "phone", nullable = false, length = 50)
    private String phone;

    @Column(name = "code", length = 60)
    private String code;

    @Column(name = "reg_date", nullable = false)
    private LocalDateTime regDate;

    @Column(name = "up_date", nullable = false)
    private LocalDateTime upDate;

    @Column(name = "acount", length = 60)
    private String account;

    @Column(name= "nick_name", nullable = false, unique = true)
    private String nickName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fammer_grade_fammer_grade_seq", nullable = false)
    private FammerGrade fammerGrade;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_seq")
    private ManagementUser managementUser;

    @OneToOne
    private Streaming streamings;

    @OneToMany(mappedBy = "farmerUser",fetch = FetchType.LAZY)
    private List<Likes> likes;

    public FarmerUser (long farmerSeq){
        this.userSeq = farmerSeq;
    }
}