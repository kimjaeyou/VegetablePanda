package web.mvc.security;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import web.mvc.dto.GetAllUserDTO;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Setter
@Getter
public class CustomMemberDetails implements UserDetails{
    private final GetAllUserDTO user;

    public CustomMemberDetails(GetAllUserDTO getUser) {
        this.user = getUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        log.info("getAuthorities!!!!!");
        Collection<GrantedAuthority> collection = new ArrayList<GrantedAuthority>();
        collection.add(()->user.getRole());
        return collection;
    }

    @Override
    public String getPassword() {
        log.info("getPassword!!!!!");

        return user.getPw();
    }

    @Override
    public String getUsername() {
        log.info("getUsername!!!!!");
        return user.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        log.info("isAccountNonExpired!!!!!");
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        log.info("isAccountNonLocked!!!!!");
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        log.info("isCredentialsNonExpired!!!!!");
        return true;
    }

    @Override
    public boolean isEnabled() {
        log.info("isEnabled!!!!!");
        return true;
    }
}