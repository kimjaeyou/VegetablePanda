package web.mvc.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notice_board")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_no_seq")
    private Integer boardNoSeq;

    @Column(name = "subject", nullable = false, length = 45)
    private String subject;

    @Column(name = "content", nullable = false, length = 500)
    private String content;

    @Column(name = "readnum", nullable = false, length = 45)
    private String readnum;

    @Column(name = "reg_date", nullable = false)
    private LocalDateTime regDate;

    @OneToOne
    @JoinColumn(name = "file_file_seq", nullable = false)
    private File file;


    NoticeBoard(String subject, String content, String readnum, LocalDateTime regDate) {

        this.subject = subject;
        this.content = content;
        this.readnum = readnum;
        this.regDate = regDate;
    }
}
