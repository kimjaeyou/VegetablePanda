package web.mvc.service;

import org.springframework.web.multipart.MultipartFile;
import web.mvc.domain.ManagementUser;
import web.mvc.dto.GetAllUserDTO;

public interface MemberService {
    String duplicateCheck(String id);

    void signUp(GetAllUserDTO member, MultipartFile image);
}
