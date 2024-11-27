package web.mvc.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QaDTO {

    private Long boardNoSeq;   // 게시글 번호
    private String subject;    // 제목
    private String content;    // 내용
    private Integer readnum;    // 조회수
    private LocalDateTime regDate; // 등록일
    private String writerId; //작성자

    public QaDTO(String subject, String content, Integer readnum, LocalDateTime regDate, String writerId) {
        this.subject = subject;
        this.content = content;
        this.readnum = readnum;
        this.regDate = regDate;
        this.writerId = writerId;
    }

    public QaDTO(QaDTO qaDTO) {

        this.writerId = qaDTO.getWriterId();
    }


}
