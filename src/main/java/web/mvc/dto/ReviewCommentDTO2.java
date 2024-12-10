package web.mvc.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
public class ReviewCommentDTO2 {
    private String content;
    private String filePath;
    private Integer score;
    private LocalDateTime date;
    private Long userSeq;

    public ReviewCommentDTO2(String content, String filePath, Integer score, LocalDateTime date, Long userSeq) {
        this.content = content;
        this.filePath = filePath != null ? filePath : "";
        this.score = score;
        this.date = date;
        this.userSeq = userSeq;
    }

}