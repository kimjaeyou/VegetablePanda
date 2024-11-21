package web.mvc.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "review_comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReviewComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_comment_seq")
    private Long reviewCommentSeq;

    @Column(name = "content", length = 80,nullable = false)
    private String content;

    @Column(name = "score", nullable = false)
    private Integer score;

    @OneToOne
    @JoinColumn(name = "file_seq", nullable = false)
    private File file;

    @Column(name="user_seq" , nullable = false)
    private Long userSeq;

    @Column(name="reg_date" , nullable = false)
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_seq", nullable = false)
    private Review review;
}