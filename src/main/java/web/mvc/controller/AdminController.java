package web.mvc.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import web.mvc.config.ApiDataCofig;
import web.mvc.domain.Member;
import web.mvc.dto.GarakAuctionRslt;
import web.mvc.security.CustomMemberDetails;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@RestController
@Slf4j
@Tag(name = "AdminController API", description = "Security Swagger 테스트용  API")
public class AdminController {
    @GetMapping("/admin")
    public String admin(){
        //시큐리티에 저장된 정보 조회
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Authentication getName =  {} " , name);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomMemberDetails customMemberDetails = (CustomMemberDetails)authentication.getPrincipal();
        Member m = customMemberDetails.getMember();
        log.info("customMemberDetails =  {} ,{} ,{} " , m.getId(), m.getName(), m.getRole());


        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        while(iter.hasNext()){
            GrantedAuthority auth = iter.next();
            String role = auth.getAuthority();
            log.info("Authentication role =  {} " , role);
        }

        return "admin 입니다.";
    }

    @GetMapping("/test/testAPI")
    public String testAPI() throws IOException {
        ApiDataCofig apiDataCofig = new ApiDataCofig();
        GarakAuctionRslt gList= apiDataCofig.Test("1","1000");

        return "testAPI";
    }

}
