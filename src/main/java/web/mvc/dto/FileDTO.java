package web.mvc.dto;

import lombok.*;
import web.mvc.domain.File;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileDTO {
    private long fileSeq;
    private String name;
    private String path;

    public FileDTO (File file) {
        this.fileSeq = file.getFileSeq();
        this.name = file.getName();
        this.path = file.getPath();
    }
}
