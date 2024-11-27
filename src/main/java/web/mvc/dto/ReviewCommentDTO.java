package web.mvc.dto;

import lombok.*;
import web.mvc.domain.ReviewComment;

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

    public ReviewCommentDTO(Long reviewCommentSeq, String content, Integer score, Long reviewSeq) {
        this.reviewCommentSeq = reviewCommentSeq;
        this.content = content;
        this.score = score;
        this.reviewSeq = reviewSeq;
    }
}