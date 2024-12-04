package web.mvc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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

    @CreationTimestamp
    @Column(name = "reg_date", nullable = false)
    private LocalDateTime createTime;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "board_no_seq", nullable = false)
    @JsonIgnore
    private QaBoard qaBoard;

}

