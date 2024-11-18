package web.mvc.security;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import web.mvc.domain.CompanyUser;
import web.mvc.domain.FarmerUser;
import web.mvc.domain.ManagementUser;
import web.mvc.domain.User;

import java.util.ArrayList;
import java.util.Collection;

@Data
@Slf4j // log.info()
public class CustomUserDetails implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        log.info("getAuthorities...");

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> "ROLE_" + user.getRole().toUpperCase());

        return authorities;
    }

    @Override
    public String getPassword() {
        log.info("getPassword...");
        return user.getPw();
    }

    @Override
    public String getUsername() {
        log.info("getUsername...");
        return user.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        log.info("isAccountNonExpired...");
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        log.info("isAccountNonLocked...");
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        log.info("isCredentialsNonExpired...");
        return true;
    }

    @Override
    public boolean isEnabled() {
        log.info("isEnabled...");
        return true;
    }
}