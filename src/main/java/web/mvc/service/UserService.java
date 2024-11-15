package web.mvc.service;

import web.mvc.domain.CompanyUser;
import web.mvc.domain.FarmerUser;
import web.mvc.domain.User;

public interface UserService {

    /**
     * 아이디 중복체크
     */
    String duplicateCheck(String id);

    /**
     * 일반 회원 가입
     */
    void userSignUp(User user);

    /**
     * 업체 회원가입
     */
    void companySignUp(CompanyUser companyUser);

    /**
     * 판매자 회원가입
     */
    void farmerSignUp(FarmerUser farmerUser);

}
