package web.mvc.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "qa_board_reply")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class QaBoardReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_seq")
    private Long replySeq;

    @Column(name = "comment", nullable = false, length = 255)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_no_seq", nullable = false)
    private QaBoard qaBoard;
}
