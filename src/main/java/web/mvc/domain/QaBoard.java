package web.mvc.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import web.mvc.dto.QaDTO;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "qa_board")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class QaBoard extends QaDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_no_seq")
    private Long boardNoSeq;

    @Column(name = "subject", nullable = false, length = 45)
    private String subject;

    @Column(name = "content", nullable = false, length = 500, columnDefinition = "TEXT")
    private String content;

    @Column(name = "readnum", nullable = false, length = 45)
    private Integer readnum;

    @CreationTimestamp
    @Column(name = "reg_date", nullable = false)
    private LocalDateTime regDate;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "file_seq")
    private File file;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    @ToString.Exclude
    private ManagementUser managementUser;

    @OneToMany(mappedBy = "qaBoard", cascade = CascadeType.ALL, orphanRemoval = true)  // 댓글 함께 삭제
    private List<QaBoardReply> replies;

}
