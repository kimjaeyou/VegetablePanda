package web.mvc.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.List;

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

    @Column(name = "pw", nullable = false, length = 100)
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

    public User(Long user_seq,String userId,String name, String pw,
                String address,String gender,String phone,
                String email,int state,String role) {
        this.userSeq = user_seq;
        this.id = userId;
        this.name = name;
        this.pw = pw;
        this.address = address;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.state = state;
        this.role = role;
        this.regDate = LocalDateTime.now();
    }
}