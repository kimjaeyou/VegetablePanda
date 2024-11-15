package web.mvc.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "likes")
@Getter
@Setter
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_seq")
    private Integer likeSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "management_user_user_seq", nullable = false)
    private ManagementUser managementUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_seq", nullable = false)
    private FarmerUser farmerUser;
}
