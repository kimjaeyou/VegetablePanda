package web.mvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.CompanyUser;
import web.mvc.domain.FarmerUser;
import web.mvc.domain.ManagementUser;
import web.mvc.domain.User;
import web.mvc.dto.GetAllUserDTO;
import web.mvc.exception.ErrorCode;
import web.mvc.exception.MemberAuthenticationException;
import web.mvc.repository.CompanyUserRepository;
import web.mvc.repository.FarmerUserRepository;
import web.mvc.repository.NormalUserRepository;
import web.mvc.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {
    private final UserRepository userRepository;
    private final FarmerUserRepository farmerRepository;
    private final NormalUserRepository normalUserRepository;
    private final CompanyUserRepository companyUserRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional(readOnly = true)
    @Override
    public String duplicateCheck(String id) {
       return"";
    }

    @Override
    @Transactional
    public void signUp(GetAllUserDTO user) {

        if(userRepository.existsById(user.getId())>0) {
            throw new MemberAuthenticationException(ErrorCode.DUPLICATED);
        }else{
            ManagementUser managementUser = new ManagementUser(user.getId(),user.getContent());
            ManagementUser m=userRepository.save(managementUser);
            log.info("member = "+m);
            if(user.getContent().equals("farmer")){
               fammerIn(m,user);
            }else if (user.getContent().equals("user")) {
                userIn(m,user);
            }else if (user.getContent().equals("company")) {
                companyIn(m,user);
            }else{
                throw new MemberAuthenticationException(ErrorCode.WRONG_PASS);
            }


        }
    }

    public void fammerIn(ManagementUser m,GetAllUserDTO user){
        FarmerUser fuser=
                new FarmerUser(
                        m.getUserSeq(),
                        user.getId(),
                        passwordEncoder.encode(user.getPw()),
                        user.getName(),
                        user.getAddress(),
                        user.getCode(),
                        user.getAcount(),
                        user.getPhone(),
                        user.getEmail(),
                        0,
                        "ROLE_FARMER"
                );
        farmerRepository.save(fuser);
    }

    public void userIn(ManagementUser m,GetAllUserDTO user){
        User uuser=
                new User(
                        m.getUserSeq(),
                        user.getId(),
                        user.getName(),
                        passwordEncoder.encode(user.getPw()),
                        user.getAddress(),
                        user.getGender(),
                        user.getPhone(),
                        user.getEmail(),
                        0,
                        "ROLE_USER"
                );
        normalUserRepository.save(uuser);
    }

    public void companyIn(ManagementUser m,GetAllUserDTO user){
        CompanyUser cuser=
                new CompanyUser(
                        m.getUserSeq(),
                        user.getId(),
                        user.getComName(),
                        user.getOwnerName(),
                        passwordEncoder.encode(user.getPw()),
                        user.getAddress(),
                        user.getPhone(),
                        user.getCode(),
                        user.getEmail(),
                        0,
                        "ROLE_COMPANY"
                );
        companyUserRepository.save(cuser);
    }


}
