package web.mvc.dto;

import lombok.*;
import web.mvc.domain.QaBoard;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QaDTO {

    private Long boardNoSeq;   // 게시글 번호
    private String subject;    // 제목
    private String content;    // 내용
    private Integer readnum;   // 조회수
    private LocalDateTime regDate; // 등록일
    private String writerId;   // 작성자

    /**
     * 엔티티를 DTO로 변환하는 정적 메서드
     */
    public static QaDTO fromEntity(QaBoard qaBoard, String writerId) {
        return QaDTO.builder()
                .boardNoSeq(qaBoard.getBoardNoSeq())
                .subject(qaBoard.getSubject())
                .content(qaBoard.getContent())
                .readnum(qaBoard.getReadnum())
                .regDate(qaBoard.getRegDate())
                .writerId(qaBoard.getManagementUser().getId())
                .build();
    }
}
