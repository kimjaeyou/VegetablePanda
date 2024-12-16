package web.mvc.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class FarmerUserDTO {
    private String userSeq;
    private String farmerId;
    private String name;
    private String pw;
    private String email;
    private String phone;
    private String grade;
    private String regDate;
    private String address;
    private String code;
    private String account;
    private String nickName;
}