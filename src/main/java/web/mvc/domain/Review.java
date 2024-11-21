package web.mvc.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "review")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_seq")
    private Long reviewSeq;

    @Column(name = "visit_num", nullable = false)
    private Integer visitNum;

    @OneToOne
    @JoinColumn(name = "user_seq", nullable = false)
    private ManagementUser managementUser;

    @OneToMany(mappedBy = "review")
    private List<ReviewComment> reviewComments;

    public Review(long management_user,Integer visitNum) {
        this.managementUser = new ManagementUser(management_user);
        this.visitNum = visitNum;
    }
}
