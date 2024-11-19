package web.mvc.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "qa_board_reply")
@Getter
@Setter
public class QaBoardReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_seq")
    private Integer replySeq;

    @Column(name = "comment", nullable = false, length = 255)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qa_board_board_no_seq", nullable = false)
    private QaBoard qaBoard;
}
