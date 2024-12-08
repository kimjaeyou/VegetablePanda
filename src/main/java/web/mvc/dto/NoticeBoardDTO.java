package web.mvc.dto;


import lombok.*;
import web.mvc.domain.File;
import web.mvc.domain.NoticeBoard;

import java.time.LocalDateTime;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class NoticeBoardDTO {
        private Long boardNoSeq;
        private String subject;
        private String content;
        private int readnum;
        private LocalDateTime regDate;
        private File file;

        // Entity -> DTO 변환
        public static NoticeBoardDTO from(NoticeBoard entity) {
            return NoticeBoardDTO.builder()
                    .boardNoSeq(entity.getBoardNoSeq())
                    .subject(entity.getSubject())
                    .content(entity.getContent())
                    .readnum(entity.getReadnum())
                    .regDate(entity.getRegDate())
                    .file(entity.getFile())
                    .build();
        }

        // DTO -> Entity 변환
        public NoticeBoard toEntity() {
            return NoticeBoard.builder()
                    .subject(this.subject)
                    .content(this.content)
                    .readnum(this.readnum)
                    .regDate(this.regDate)
                    .file(this.file)
                    .build();
        }
    }
