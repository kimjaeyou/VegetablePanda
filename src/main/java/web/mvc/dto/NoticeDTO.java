package web.mvc.dto;

import lombok.*;
import web.mvc.domain.File;
import java.time.LocalDateTime;

public class NoticeDTO {

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

        // 파일 정보 포함
        private File file;
    }
}
