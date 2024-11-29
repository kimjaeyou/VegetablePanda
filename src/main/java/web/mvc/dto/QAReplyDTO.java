package web.mvc.dto;

import lombok.*;

@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QAReplyDTO {

    private Long boardNoSeq; // 게시글 번호
    private String comment;
}
