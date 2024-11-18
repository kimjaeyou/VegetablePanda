package web.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FarmerUserDTO {
    private String user_seq;
    private String farmer_id;
    private String name;
    private String pw;
    private String address;
    private String code;
    private String account;
    private String nickName;


}