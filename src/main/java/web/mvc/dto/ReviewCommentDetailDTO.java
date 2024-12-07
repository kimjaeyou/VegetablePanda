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

}