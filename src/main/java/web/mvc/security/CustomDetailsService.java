package web.mvc.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import web.mvc.domain.CompanyUser;
import web.mvc.domain.FarmerUser;
import web.mvc.domain.ManagementUser;
import web.mvc.domain.User;
import web.mvc.repository.CompanyRepository;
import web.mvc.repository.FarmerRepository;
import web.mvc.repository.ManagementRepository;
import web.mvc.repository.UserRepository;

import java.util.List;

/**
 * 인증처리  서비스 - repository에서 정보를 찾아서 인증,,
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final FarmerRepository FarmerRepository;
    private final ManagementRepository managementRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("username = {}", username); // 아이디 들어왔나 확인

        // DB에 전달받은 id에 해당하는 정보가 있는지 찾기
        ManagementUser findId = managementRepository.findById(username);
        log.info("findMember = {}", findId);
        if (findId != null) {
            log.info("일단 아이디 찾았고 = {}", findId);

            // 이제 찾은 아이디로 content에 맞는 ROLE을 찾는다.
            String content = findId.getContent();
            log.info("ROLE이 뭐야? = {}", content);

            if (content == "ROLE_USER") {
                List<User> userList = userRepository.findUser(findId.getId());
                // 찾았으면 그 찾은 리스트를 여기에 출력
                log.info("일반 회원 = {}", userList);
                return new CustomUserDetails((User) userList); // UserDetails

            } else if (content == "ROLE_COMPANY") {
                List<CompanyUser> companyList = companyRepository.findUser(findId.getId());
                // 찾았으면 그 찾은 리스트를 여기에 출력
                log.info("업체 회원 = {}", companyList);
                return new CustomCompanyDetails((CompanyUser) companyList); // UserDetails

            } else if (content == "ROLE_FARMER") {
                List<FarmerUser> farmerList = FarmerRepository.findUser(findId.getId());
                // 찾았으면 그 찾은 리스트를 여기에 출력
                log.info("판매자 = {}", farmerList);
                return new CustomFarmerDetails((FarmerUser) farmerList); // UserDetails
            }
        }
        return null;
    }
}