package web.mvc.service;

import web.mvc.domain.ManagementUser;
import web.mvc.dto.GetFammerUserDTO;

public interface MemberService {
    String duplicateCheck(String id);

    void signUp(GetFammerUserDTO member);
}
