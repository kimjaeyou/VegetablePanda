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

    @Column(name = "content", length = 505)
    private String content;

    @OneToOne
    @JoinColumn(name = "file_file_seq", nullable = false)
    private File file;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "managementUser_user_seq", nullable = false)
    private ManagementUser managementUser;

    @OneToOne
    @JoinColumn(name = "farmer_seq", nullable = false)
    private FarmerUser farmerUser;

    @OneToMany(mappedBy = "review")
    private List<ReviewComment> reviewComments;
}
