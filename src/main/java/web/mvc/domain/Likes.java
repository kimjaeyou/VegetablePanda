package web.mvc.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "likes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_seq")
    private Long likeSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private ManagementUser managementUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_seq", nullable = false)
    private FarmerUser farmerUser;

    @Column(name = "state")
    private Boolean state;

    public Likes(Long userSeq, Long farmerSeq) {
        this.managementUser = new ManagementUser(userSeq);
        this.farmerUser = new FarmerUser(farmerSeq);
    }
}
