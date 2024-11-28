package web.mvc.dto;

import lombok.*;
import web.mvc.domain.ReviewComment;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewCommentDTO {
    private Long reviewCommentSeq;
    private String content;
    private Integer score;
    private Long reviewSeq;
    private String id;
    private String filePath;
    private LocalDateTime date;
    private Long userSeq;

    public ReviewCommentDTO(Long reviewCommentSeq, String content, Integer score, Long reviewSeq) {
        this.reviewCommentSeq = reviewCommentSeq;
        this.content = content;
        this.score = score;
        this.reviewSeq = reviewSeq;
    }

    public ReviewCommentDTO(String content, String filePath, Integer score, LocalDateTime date, Long userSeq) {
        this.content = content;
        this.filePath = filePath != null ? filePath : "";
        this.score = score;
        this.date = date;
        this.userSeq = userSeq;
    }

}