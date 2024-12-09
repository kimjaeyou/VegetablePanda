package web.mvc.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FarmerUserDTO2 {
    private Long userSeq;
    private String farmerId;
    private String name;
    private String pw;
    private String email;
    private String code;
    private String address;
    private String phone;
    private String grade;
    private LocalDateTime regDate;
    private String account;
    private String path;
    private String intro;
    private Integer count;

    public FarmerUserDTO2(Long userSeq,
                          String farmerId,
                          String name,
                          String email,
                          String code,
                          String address,
                          String phone,
                          String grade,
                          LocalDateTime regDate,
                          String account,
                          String intro,
                          String path) {
        this.userSeq = userSeq;
        this.farmerId = farmerId;
        this.name = name;
        this.email = email;
        this.code = code;
        this.address = address;
        this.phone = phone;
        this.grade = grade;
        this.regDate = regDate;
        this.account = account;
        this.intro = intro;
        this.path = path;
    }

    public FarmerUserDTO2 (Long userSeq, String name, String path , String intro) {
        this.userSeq = userSeq;
        this.name = name;
        this.path = path;
        this.intro = intro;
    }

    public FarmerUserDTO2 (Long userSeq, String name, String path , String intro,Integer count) {
        this.userSeq = userSeq;
        this.name = name;
        this.path = path;
        this.intro = intro;
        this.count = count;
    }
}
