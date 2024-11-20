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
    @JoinColumn(name = "farmer_seq", nullable = false)
    private FarmerUser farmerUser;

    @OneToMany(mappedBy = "review")
    private List<ReviewComment> reviewComments;
}
