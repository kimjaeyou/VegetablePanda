package web.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import web.mvc.domain.CompanyUser;
import web.mvc.domain.FarmerUser;
import web.mvc.domain.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllUserDTO {
    private Long user_seq;
    private String id;
    private String pw;
    private String content;
    private String name;
    private String address;
    private String email;
    private String phone;
    private String code;
    private String acount;
    private String gender;
    private String ownerName;
    private String comName;
    private String role;

    public GetAllUserDTO(FarmerUser fuser) {
        this.user_seq=fuser.getUser_seq();
        this.id=fuser.getFarmerId();
        this.name= fuser.getName();
        this.pw = fuser.getPw();
        this.address=fuser.getAddress();
        this.code=fuser.getCode();
        this.acount=fuser.getAccount();
        this.email=fuser.getEmail();
        this.phone=fuser.getPhone();
        this.role=fuser.getRole();
    }

    public GetAllUserDTO(User nuser) {
        this.user_seq=nuser.getUserSeq();
        this.id=nuser.getId();
        this.name= nuser.getName();
        this.pw = nuser.getPw();
        this.address=nuser.getAddress();
        this.code=nuser.getGender();
        this.email=nuser.getEmail();
        this.phone=nuser.getPhone();
        this.role=nuser.getRole();
    }

    public GetAllUserDTO(CompanyUser cuser) {
        this.user_seq=cuser.getUserSeq();
        this.id=cuser.getCompanyId();
        this.name= cuser.getComName();
        this.ownerName=cuser.getOwnerName();
        this.pw = cuser.getPw();
        this.address=cuser.getAddress();
        this.email=cuser.getEmail();
        this.phone=cuser.getPhone();
        this.role=cuser.getRole();
        this.code=cuser.getCode();
    }

}
