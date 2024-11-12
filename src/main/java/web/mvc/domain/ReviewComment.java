package web.mvc.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "review_comment")
@Data
public class ReviewComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_comment_seq")
    private Integer reviewCommentSeq;

    @Column(name = "content", length = 80)
    private String content;

    @Column(name = "score")
    private Integer score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_seq", nullable = false)
    private Review review;
}
