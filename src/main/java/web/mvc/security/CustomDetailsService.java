package web.mvc.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import web.mvc.domain.*;
import web.mvc.dto.GetAllUserDTO;
import web.mvc.repository.*;

import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class CustomDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final NormalUserRepository nUserRepository;
    private final CompanyUserRepository cUserRepository;
    private final FarmerUserRepository fUserRepository;
    private GetAllUserDTO getAllUserDTO;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername -----------> : ", username);
        System.out.println(username);
        ManagementUser m= userRepository.findById(username);
        System.out.println("!!!!!!!!!!!!!"+m);
        if(m!=null){
            log.info("FindUser -----------> : ", m);
            String role=m.getContent();
            if(role.equals("farmer")){
                Optional<FarmerUser> fa = fUserRepository.findById(m.getUserSeq());
                FarmerUser fUser=fa.get();
                getAllUserDTO=new GetAllUserDTO(fUser);

            } else if (role.equals("user")) {
                Optional<User> na =nUserRepository.findById(m.getUserSeq());
                User nUser=na.get();
                getAllUserDTO=new GetAllUserDTO(nUser);

            } else if (role.equals("company")) {
                Optional<CompanyUser> ca =cUserRepository.findById(m.getUserSeq());
                CompanyUser cUser=ca.get();
                getAllUserDTO=new GetAllUserDTO(cUser);

            }

            return new CustomMemberDetails(getAllUserDTO);
        }else{
            throw new UsernameNotFoundException(username);
        }
    }
}