package web.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCommentDTO {
    private Long reviewCommentSeq;
    private String content;
    private Integer score;
    private Long reviewSeq;
}
