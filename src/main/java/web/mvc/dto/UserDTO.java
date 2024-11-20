package web.mvc.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long userSeq;
    private String id;
    private String pw;
    private String name;
    private String address;
    private String phone;
    private Integer state;
    private String gender;
    private String email;
}
