package web.mvc.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "file")
@Data
public class File {
    @Id
    @Column(name = "file_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fileSeq;

    @Column(name = "path", length = 300)
    private String path;

    @Column(name = "name", length = 80)
    private String name;
}
