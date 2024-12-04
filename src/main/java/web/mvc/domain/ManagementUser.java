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
    private Review review;

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

    @OneToMany(mappedBy = "managementUser",fetch = FetchType.LAZY)
    private List<QaBoard> qaBoard;

    @OneToMany(mappedBy = "managementUser",fetch = FetchType.LAZY)
    private List<ReviewComment> reviewCommentList;

    @OneToMany(mappedBy = "managementUser",fetch = FetchType.LAZY)
    private List<ShopLike> shopLikes;


    @OneToOne
    @JoinColumn(name = "file_seq")
    private File file;

    public ManagementUser(String id, String content) {
        this.id = id;
        this.content = content;
    }

    public ManagementUser(String id, String content, File file) {
        this.id = id;
        this.content = content;
        this.file = file;
    }


    public ManagementUser(Long userSeq) {
        this.userSeq = userSeq;
    };

}
