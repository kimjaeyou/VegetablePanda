package web.mvc.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long userSeq;
    private String id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String gender;
    private LocalDateTime regDate;
    private String path;

    public UserDTO(Long userSeq, String id, String name, String address, String email, String phone, String gender, LocalDateTime regDate, String path) {
        this.userSeq = userSeq;
        this.path = path;
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.regDate = regDate;
    }

}