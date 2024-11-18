package web.mvc.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import web.mvc.dto.GetAllUserDTO;
import web.mvc.repository.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername = {} ", username);
        GetAllUserDTO getAllUserDTO = userRepository.findById(username);

        if(getAllUserDTO != null){
            log.info("FindMem = {} ", getAllUserDTO);
            return new CustomUserDetails(getAllUserDTO);
        }
        return null;
    }
}