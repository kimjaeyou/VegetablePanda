package web.mvc.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "management_user")
@Data
public class ManagementUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    private Integer userSeq;
    
    @Column(name = "content", nullable = false, length = 50)
    private String content;

    @OneToOne(mappedBy = "managementUser")
    private FarmerUser farmerUser;

    @OneToOne(mappedBy = "managementUser")
    private CompanyUser companyUser;

    @OneToOne(mappedBy = "managementUser")
    private User user;

    @OneToMany(mappedBy = "managementUser",fetch = FetchType.LAZY)
    private List<Review> reviews;
    
    @OneToOne(mappedBy = "managementUser")
    private UserWallet userWallet;
    
    @OneToMany(mappedBy = "managementUser",fetch = FetchType.LAZY)
    private List<Message> messages;
    
    @OneToMany(mappedBy = "managementUser",fetch = FetchType.LAZY)
    private List<Bid> bids;
    
    @OneToMany(mappedBy = "managementUser",fetch = FetchType.LAZY)
    private List<Like> likes;
    
    @OneToMany(mappedBy = "managementUser",fetch = FetchType.LAZY)
    private List<CalcPoint> calcPoints;
}
