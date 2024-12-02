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
    private Long reviewCommentSeq;  // 댓글 고유 번호
    private String content;         // 댓글 내용
    private Integer score;          // 평점
    private String filePath;        // 파일 경로
    private String fileName;        // 파일 이름
    private Long userId;            // 작성자 ID
    private LocalDateTime regDate;  // 작성일
    private Long reviewSeq;         // 연결된 리뷰 ID
    private Long userBuyDetailSeq;  // 구매 정보 ID

    // 엔티티를 DTO로 변환하는 정적 메서드
    public static ReviewCommentDTO fromEntity(ReviewComment reviewComment) {
        return ReviewCommentDTO.builder()
                .reviewCommentSeq(reviewComment.getReviewCommentSeq())
                .content(reviewComment.getContent())
                .score(reviewComment.getScore())
                .filePath(reviewComment.getFile() != null ? reviewComment.getFile().getPath() : null)
                .fileName(reviewComment.getFile() != null ? reviewComment.getFile().getName() : null)
                .userId(reviewComment.getManagementUser().getUserSeq())
                .regDate(reviewComment.getDate())
                .reviewSeq(reviewComment.getReview().getReviewSeq())
                .userBuyDetailSeq(reviewComment.getUserBuyDetail().getUserBuySeq())
                .build();
    }

    // DTO를 엔티티로 변환하는 메서드
    public ReviewComment toEntity() {
        return ReviewComment.builder()
                .reviewCommentSeq(reviewCommentSeq)
                .content(content)
                .score(score)
                .build();
    }
}
