package web.mvc.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import web.mvc.domain.ManagementUser;
import web.mvc.domain.User;
import web.mvc.repository.ManagementRepository;
import web.mvc.repository.UserRepository;

/**
 * 인증처리  서비스 - repository에서 정보를 찾아서 인증,,
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ManagementRepository managementRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("username = {}", username);

        // DB에 id에 해당하는 정보가 있는지 찾기
        ManagementUser findMember = managementRepository.findById(username);
        log.info("findMember = {}", findMember);
        if (findMember != null) {
            log.info("아이디 찾았다 = {}", findMember);
            return new CustomMemberDetails(findMember); // UserDetails
        }
        return null;
    }
}