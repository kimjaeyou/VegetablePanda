package web.mvc.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class FarmerUserDTO2 {
    private Long userSeq;
    private String farmerId;
    private String name;
    private String pw;
    private String email;
    private String phone;
    private String grade;
    private LocalDateTime regDate;
    private String address;
    private String code;
    private String account;
    private String path;

    public FarmerUserDTO2(Long userSeq, String farmerId, String name, String pw, String email, String phone,
                  String grade, LocalDateTime regDate, String address, String code, String account, String path) {
        this.userSeq = userSeq;
        this.farmerId = farmerId;
        this.name = name;
        this.pw = pw;
        this.email = email;
        this.phone = phone;
        this.grade = grade;
        this.regDate = regDate;
        this.address = address;
        this.code = code;
        this.account = account;
        this.path = path;
    }
}