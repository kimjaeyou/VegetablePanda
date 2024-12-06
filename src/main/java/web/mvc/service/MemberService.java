package web.mvc.service;

import org.springframework.web.multipart.MultipartFile;
import web.mvc.domain.FarmerUser;
import web.mvc.domain.ManagementUser;
import web.mvc.dto.FarmerUserDTO2;
import web.mvc.dto.GetAllUserDTO;

import java.util.List;

public interface MemberService {
    String duplicateCheck(String id);

    void signUp(GetAllUserDTO member, MultipartFile image);

    List<FarmerUserDTO2> farmer();
}
