package web.mvc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "qa_board_reply")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class QaBoardReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_seq")
    private Long replySeq;

    @Column(name = "comment", nullable = false, length = 255)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_no_seq", nullable = false)
    @JsonIgnore
    private QaBoard qaBoard;

    QaBoardReply(QaBoard qaBoard, String comment) {

        this.qaBoard = qaBoard;
        this.comment = comment;
    }
}
