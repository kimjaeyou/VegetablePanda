package web.mvc.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_profile")
@Data
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_profile_seq")
    private Integer userProfileSeq;

    @Column(name = "file_path", length = 60)
    private String filePath;

    @Column(name = "file_size")
    private Integer fileSize;

    @OneToOne
    @JoinColumn(name = "user_seq", nullable = false)
    private ManagementUser managementUser;
}
