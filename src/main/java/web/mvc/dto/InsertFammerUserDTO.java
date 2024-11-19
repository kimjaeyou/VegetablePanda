package web.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InsertFammerUserDTO {
    private Long user_seq;
    private String farmer_id;
    private String name;
    private String pw;
    private String address;
    private String code;
    private String account;
    private String phone;
    private String email;
    private int state;
    private String role;
}
