package web.mvc.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class ReviewDTO {
    private Long reviewCommentSeq;
    private Integer score;
    private String content;
    private String name;
    private LocalDateTime date;
}
