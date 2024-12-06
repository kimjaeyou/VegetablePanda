package web.mvc.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class ReviewDTO2 {
    private Long reviewSeq;        // 리뷰 고유 ID
    private Integer visitNum;      // 방문자 수
    private String intro;          // 한 줄 소개
    private Long managementUserId; // 작성자 (판매자) ID
    private LocalDateTime lastCommentDate; // 마지막 댓글 작성 날짜
}

