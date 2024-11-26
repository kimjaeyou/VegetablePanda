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
    private String name;
    private String address;
    private String phone;
    private String email;
    private String gender;
    private String regDate;
}