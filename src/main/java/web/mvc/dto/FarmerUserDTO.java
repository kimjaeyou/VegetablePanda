package web.mvc.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FarmerUserDTO {
    private long user_seq;

    private String farmer_id;
    private String name;
    private String pw;
    private String address;
    private String code;
    private String account;
    private String nickName;


}
