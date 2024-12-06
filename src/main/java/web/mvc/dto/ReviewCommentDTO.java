package web.mvc.dto;

import lombok.*;
import web.mvc.domain.ReviewComment;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ReviewCommentDTO {
    private Long reviewCommentSeq;
    private String content;
    private Integer score;
    private Long userSeq;
    private LocalDateTime regDate;
    private Long reviewSeq; // 댓글이 속한 게시판(Review)의 ID
    private Long userBuyDetailSeq;
    private FileDTO file;



    // 엔티티를 DTO로 변환하는 정적 메서드
    public static ReviewCommentDTO fromEntity(ReviewComment reviewComment) {
        return ReviewCommentDTO.builder()
                .reviewCommentSeq(reviewComment.getReviewCommentSeq())
                .content(reviewComment.getContent())
                .score(reviewComment.getScore())
                .userSeq(reviewComment.getUserBuyDetail().getUserBuy().getManagementUser().getUserSeq())
                .regDate(reviewComment.getDate())
                .reviewSeq(reviewComment.getReview().getReviewSeq()) // 게시판 ID 설정
                .userBuyDetailSeq(reviewComment.getUserBuyDetail().getUserBuySeq())
                .build();
    }

    public ReviewCommentDTO (Long reviewCommentSeq, String content, Integer score , Long reviewSeq) {
        this.reviewCommentSeq = reviewCommentSeq;
        this.content = content;
        this.score = score;
        this.reviewSeq = reviewSeq;
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