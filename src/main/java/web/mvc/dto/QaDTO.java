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
@Builder(builderMethodName = "qaDTOBuilder") // 빌더 메서드 이름 변경
public class QaDTO {

    private Long boardNoSeq;   // 게시글 번호
    private String subject;    // 제목
    private String content;    // 내용
    private Integer readnum;   // 조회수
    private LocalDateTime regDate; // 등록일
    private String filePath;   // 파일 경로 (선택적 포함)
    private String fileName;   // 파일 이름 (선택적 포함)
    private String writerId;   // 작성자

    /**
     * 엔티티를 DTO로 변환하는 정적 메서드
     * 파일 데이터를 포함하지 않음
     */
    public static QaDTO fromEntity(QaBoard qaBoard) {
        return fromEntity(qaBoard, false); // 기본적으로 파일 데이터 포함 안 함
    }

    /**
     * 엔티티를 DTO로 변환하는 정적 메서드
     * 파일 데이터를 포함할지 여부를 제어
     */
    public static QaDTO fromEntity(QaBoard qaBoard, boolean includeFile) {
        QaDTO.QaDTOBuilder builder = QaDTO.qaDTOBuilder()
                .boardNoSeq(qaBoard.getBoardNoSeq())
                .subject(qaBoard.getSubject())
                .content(qaBoard.getContent())
                .readnum(qaBoard.getReadnum())
                .regDate(qaBoard.getRegDate())
                .writerId(qaBoard.getManagementUser().getId());

        // 파일 데이터 포함 여부를 확인
        if (includeFile && qaBoard.getFile() != null) {
            builder.filePath(qaBoard.getFile().getPath())
                    .fileName(qaBoard.getFile().getName());
        }

        return builder.build();
    }

    public static QaDTO fromEntity(QaBoard qaBoard, String writerId) {
        return QaDTO.qaDTOBuilder()
                .boardNoSeq(qaBoard.getBoardNoSeq())
                .subject(qaBoard.getSubject())
                .content(qaBoard.getContent())
                .readnum(qaBoard.getReadnum())
                .regDate(qaBoard.getRegDate())
                .filePath(qaBoard.getFilePath())
                .fileName(qaBoard.getFileName())
                .writerId(writerId) // 전달받은 writerId 사용
                .build();
    }

    /**
     * DTO를 엔티티로 변환하는 메서드
     */
    public QaBoard toEntity() {
        return QaBoard.builder()
                .boardNoSeq(this.boardNoSeq)
                .subject(this.subject)
                .content(this.content)
                .readnum(this.readnum == null ? 0 : this.readnum)
                .regDate(this.regDate)
                .build();
    }
}
