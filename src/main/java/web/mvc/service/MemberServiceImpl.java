package web.mvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.FarmerUser;
import web.mvc.domain.ManagementUser;
import web.mvc.domain.Member;
import web.mvc.dto.FarmerUserDTO;
import web.mvc.dto.GetFammerUserDTO;
import web.mvc.dto.InsertFammerUserDTO;
import web.mvc.exception.ErrorCode;
import web.mvc.exception.MemberAuthenticationException;
import web.mvc.repository.FammerUserRepository;
import web.mvc.repository.MemberRepository;
import web.mvc.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final FammerUserRepository fammerRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional(readOnly = true)
    @Override
    public String duplicateCheck(String id) {
        Member member = memberRepository.duplicateCheck(id);
        System.out.println("member = "+member);
        if(member == null) return "사용가능합니다.";
        else return "중복입니다";
    }

    @Override
    public void signUp(GetFammerUserDTO user) {
        if(userRepository.existsById(user.getId())>0) {
            throw new MemberAuthenticationException(ErrorCode.DUPLICATED);
        }else{
            ManagementUser managementUser = new ManagementUser(user.getContent(),user.getId());
            ManagementUser m=userRepository.save(managementUser);
            log.info("member = "+m);
            FarmerUser fuser=
                    new FarmerUser(
                            m.getUserSeq(),
                            user.getId(),
                            user.getPassword(),
                            user.getName(),
                            user.getAddress(),
                            user.getCode(),
                            user.getAcount(),
                            user.getPhone(),
                            user.getEmail(),
                            0,
                            "ROLE_FAMMER",
                            0
                    );
            fammerRepository.save(fuser);

        }





    }
}
