package web.mvc.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.Builder;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Builder
public class User {
    @Id
    @Column(name = "user_seq")
    private Long userSeq;

    @Column(name = "user_id", nullable = false, length = 60, unique = true)
    private String id;

    @Column(name = "pw", nullable = false, length = 60)
    private String pw;

    @Column(name = "name", nullable = false, length = 60)
    private String name;

    @Column(name = "address", nullable = false, length = 300)
    private String address;

    @Column(name = "phone", nullable = false, length = 50)
    private String phone;

    @Column(name = "state", nullable = false)
    private Integer state;

    @Column(name = "gender", nullable = false , length = 10)
    private String gender;

    @Column(name = "email", nullable = false , length = 50)
    private String email;

    @Column(name = "reg_date", nullable = false)
    private LocalDateTime regDate;

    private String role;
}