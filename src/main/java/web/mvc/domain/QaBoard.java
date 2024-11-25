package web.mvc.domain;

import jakarta.persistence.*;
import lombok.*;

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
public class QaBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_no_seq")
    private Long boardNoSeq;

    @Column(name = "subject", nullable = false, length = 45)
    private String subject;

    @Column(name = "content", nullable = false, length = 500)
    private String content;

    @Column(name = "readnum", nullable = false, length = 45)
    private String readnum;

    @Column(name = "reg_date", nullable = false)
    private LocalDateTime regDate;

    @OneToOne
    @JoinColumn(name = "file_seq", nullable = true)
    private File file;


}
