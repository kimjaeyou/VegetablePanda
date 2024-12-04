package web.mvc.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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

    @Column(name="intro", length = 505) // 판매자 개인 페이지의 한줄소개
    private String intro; // 우리가 그토록 토론한 content 이거로 이름 바꿧어여

    @OneToOne
    @JoinColumn(name = "user_seq", nullable = false)
    private ManagementUser managementUser;

    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE)
    private List<ReviewComment> reviewComments;

    public Review(long management_user,Integer visitNum,String intro) {
        this.managementUser = new ManagementUser(management_user);
        this.visitNum = visitNum;
        this.intro = intro;
    }
}
