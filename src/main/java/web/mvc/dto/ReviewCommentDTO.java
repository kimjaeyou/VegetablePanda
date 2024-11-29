package web.mvc.dto;

import lombok.*;

@Data
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

    public ReviewCommentDTO(Long reviewCommentSeq, String content, Integer score, Long reviewSeq) {

        
    }
}