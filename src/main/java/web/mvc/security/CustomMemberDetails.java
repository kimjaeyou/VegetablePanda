package web.mvc.security;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import web.mvc.domain.Member;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Data
public class CustomMemberDetails implements UserDetails{
    private final Member member;

    public CustomMemberDetails(Member member) {
        this.member = member;
        log.info("CustomMemberDetails created ::::::",this.member);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        log.info("getAuthorities!!!!!");
        Collection<GrantedAuthority> collection = new ArrayList<GrantedAuthority>();
        collection.add(()->member.getRole());
        return collection;
    }

    @Override
    public String getPassword() {
        log.info("getPassword!!!!!");

        return member.getPassword();
    }

    @Override
    public String getUsername() {
        log.info("getUsername!!!!!");
        return member.getId();
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
