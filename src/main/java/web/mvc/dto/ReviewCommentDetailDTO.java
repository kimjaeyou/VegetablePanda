package web.mvc.dto;

import lombok.*;
import web.mvc.domain.File;
import web.mvc.domain.ReviewComment;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ReviewCommentDetailDTO {
    private Long reviewCommentSeq;
    private Long userSeq;
    private String productName;
    private String content;
    private Integer score;
    private String name ;
    private LocalDateTime regDate;
    private String path;
    private String profilePath;

    public ReviewCommentDetailDTO(Long reviewCommentSeq, Long userSeq, String productName, String content, Integer score, String name, LocalDateTime regDate, String path) {
        this.reviewCommentSeq = reviewCommentSeq;
        this.userSeq = userSeq;
        this.productName = productName;
        this.content = content;
        this.score = score;
        this.name = name;
        this.regDate = regDate;
        this.path = path;
    }

}