package web.mvc.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "management_user")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManagementUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(allocationSize = 1,sequenceName = "user_seq", name = "user_seq", initialValue = 1)
    @Column(name = "user_seq")
    private Long userSeq;

    @Column(name = "content", nullable = false, length = 50)
    private String content;

    @Column(name = "user_id", nullable = false, length = 50, unique = true)
    private String id;

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
    private List<Likes> likes;

    @OneToMany(mappedBy = "managementUser",fetch = FetchType.LAZY)
    private List<CalcPoint> calcPoints;


    public ManagementUser(String id,String content) {
        this.id = id;
        this.content = content;
    }
}
