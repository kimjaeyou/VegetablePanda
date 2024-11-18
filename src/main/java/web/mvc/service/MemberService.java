package web.mvc.service;

import web.mvc.domain.ManagementUser;
import web.mvc.dto.GetAllUserDTO;

public interface MemberService {
    String duplicateCheck(String id);

    void signUp(GetAllUserDTO member);
}
