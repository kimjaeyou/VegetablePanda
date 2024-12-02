package web.mvc.domain;

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

    @OneToOne(mappedBy = "file", fetch = FetchType.LAZY)
    private ManagementUser managementUser;

    @OneToOne(mappedBy = "file",fetch = FetchType.LAZY)
    private Stock stock;

    @OneToOne(mappedBy = "file",fetch = FetchType.LAZY)
    private QaBoard qaBoard;

    @OneToOne(mappedBy = "file",fetch = FetchType.LAZY)
    private ReviewComment reviewComment;

//    @JsonCreator
//    public File(@JsonProperty("fileSeq") Long fileSeq) {
//        this.fileSeq = fileSeq;
//    }

    public File(String path) {
        this.path = path;
    }

    public File(String path, String name) {
        this.path = path;
        this.name = name;
    }
}
