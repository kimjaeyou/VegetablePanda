package web.mvc.dto;

import lombok.*;
import web.mvc.domain.File;
import web.mvc.domain.QaBoard;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "qaDTOBuilder")
public class QaDTO {

    private Long boardNoSeq;   // 게시글 번호
    private String subject;    // 제목
    private String content;    // 내용
    private Integer readnum;   // 조회수
    private LocalDateTime regDate; // 등록일
    private String writerId;   // 작성자

    // 파일 정보를 포함하는 객체 추가
    private FileDTO fileDTO;

    /**
     * 엔티티를 DTO로 변환하는 정적 메서드
     */
    public static QaDTO fromEntity(QaBoard qaBoard, String writerId) {
        QaDTO.QaDTOBuilder builder = QaDTO.qaDTOBuilder()
                .boardNoSeq(qaBoard.getBoardNoSeq())
                .subject(qaBoard.getSubject())
                .content(qaBoard.getContent())
                .readnum(qaBoard.getReadnum())
                .regDate(qaBoard.getRegDate())
                .writerId(writerId);

        // 파일 정보가 있는 경우 DTO로 매핑
        if (qaBoard.getFile() != null) {
            builder.fileDTO(new FileDTO(qaBoard.getFile().getFileSeq(),
                    qaBoard.getFile().getName(),
                    qaBoard.getFile().getPath()));
        }

        return builder.build();
    }

    /**
     * DTO를 엔티티로 변환하는 메서드
     */
    public QaBoard toEntity() {
        QaBoard.QaBoardBuilder builder = QaBoard.builder()
                .boardNoSeq(this.boardNoSeq)
                .subject(this.subject)
                .content(this.content)
                .readnum(this.readnum == null ? 0 : this.readnum)
                .regDate(this.regDate);

        // 파일 정보가 있는 경우 엔티티 매핑
        if (this.fileDTO != null) {
            builder.file(new File(this.fileDTO.getFileSeq(), this.fileDTO.getName(), this.fileDTO.getPath()));
        }

        return builder.build();
    }
}
