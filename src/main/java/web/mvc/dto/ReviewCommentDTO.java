package web.mvc.dto;

import lombok.*;
import web.mvc.domain.ReviewComment;

import java.time.LocalDateTime;

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
    private String file; // File 경로 정보만 포함
    private Long userId; //유저의 ID만 포함
    private LocalDateTime date;
    private Long reviewSeq;

    // 엔티티를 DTO로 변환하는 정적 메서드
    public static ReviewCommentDTO fromEntity(ReviewComment reviewComment) {
        return ReviewCommentDTO.builder()
                .reviewCommentSeq(reviewComment.getReviewCommentSeq())
                .content(reviewComment.getContent())
                .score(reviewComment.getScore())
                .file(reviewComment.getFile() != null ? reviewComment.getFile().getPath() : null)
                .userId(reviewComment.getManagementUser().getUserSeq())
                .userId(reviewComment.getManagementUser().getUserSeq())
                .date(reviewComment.getDate())
                .reviewSeq(reviewComment.getReview().getReviewSeq())
                .build();
    }

    // DTO를 엔티티로 변환하는 메서드
    public ReviewComment toEntity() {
        return ReviewComment.builder()
                .reviewCommentSeq(reviewCommentSeq)
                .content(content)
                .score(score)
                .date(date)
                .build();
    }
}