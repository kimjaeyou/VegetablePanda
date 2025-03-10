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
    private Long userSeq;
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
    private String regName;
    private String image;
    private String intro;
    private String path;
    private Integer state;


    public GetAllUserDTO(FarmerUser fuser) {

        this.userSeq=fuser.getUserSeq();
        this.id=fuser.getFarmerId();
        this.name= fuser.getName();
        this.pw = fuser.getPw();
        this.address=fuser.getAddress();
        this.code=fuser.getCode();
        this.acount=fuser.getAccount();
        this.email=fuser.getEmail();
        this.phone=fuser.getPhone();
        this.role=fuser.getRole();
        this.state=fuser.getState();
    }

    public GetAllUserDTO(User nuser) {
        this.userSeq=nuser.getUserSeq();
        this.id=nuser.getId();
        this.name= nuser.getName();
        this.pw = nuser.getPw();
        this.address=nuser.getAddress();
        this.code=nuser.getGender();
        this.email=nuser.getEmail();
        this.phone=nuser.getPhone();
        this.role=nuser.getRole();
        this.state=nuser.getState();
    }

    public GetAllUserDTO(CompanyUser cuser) {
        this.userSeq=cuser.getUserSeq();
        this.id=cuser.getCompanyId();
        this.name= cuser.getComName();
        this.ownerName=cuser.getOwnerName();
        this.pw = cuser.getPw();
        this.address=cuser.getAddress();
        this.email=cuser.getEmail();
        this.phone=cuser.getPhone();
        this.role=cuser.getRole();
        this.code=cuser.getCode();
        this.regName=cuser.getRegName();
        this.state=cuser.getState();
    }

}
