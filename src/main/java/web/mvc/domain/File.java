package web.mvc.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "file")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class File {
    @Id
    @Column(name = "file_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileSeq;

    @Column(name = "path", length = 300)
    private String path;

    @Column(name = "name", length = 80)
    private String name;

    @JsonCreator
    public File(@JsonProperty("fileSeq") Long fileSeq) {
        this.fileSeq = fileSeq;
    }
}
