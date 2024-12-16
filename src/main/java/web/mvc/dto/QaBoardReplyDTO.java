package web.mvc.dto;

import lombok.*;
import web.mvc.domain.QaBoardReply;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QaBoardReplyDTO {
    private Long replySeq;  // 댓글의 고유 식별자
    private String comment; // 댓글 내용
    private Long boardNoSeq; // 연관된 QaBoard의 ID
    private LocalDateTime createTime; // 댓글 작성 시간

    /**
     * 엔티티를 DTO로 변환
     */
    public static QaBoardReplyDTO fromEntity(QaBoardReply qaBoardReply) {
        return QaBoardReplyDTO.builder()
                .replySeq(qaBoardReply.getReplySeq())
                .comment(qaBoardReply.getComment())
                .boardNoSeq(
                        qaBoardReply.getQaBoard() != null ?
                                qaBoardReply.getQaBoard().getBoardNoSeq() : null
                )
                .createTime(qaBoardReply.getCreateTime()) // 작성 시간 추가
                .build();
    }

    /**
     * DTO를 엔티티로 변환
     */
    public QaBoardReply toEntity() {
        return QaBoardReply.builder()
                .replySeq(replySeq)
                .comment(comment)
                .createTime(createTime) // 작성 시간 포함
                .build(); // 연관 엔티티는 서비스 계층에서 설정해야 함
    }
}
