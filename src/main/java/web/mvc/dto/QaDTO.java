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


}
