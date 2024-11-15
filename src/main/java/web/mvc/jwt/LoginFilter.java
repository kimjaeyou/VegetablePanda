package web.mvc.jwt;

import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import web.mvc.domain.CompanyUser;
import web.mvc.domain.FarmerUser;
import web.mvc.domain.ManagementUser;
import web.mvc.domain.User;
import web.mvc.security.CustomMemberDetails;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter { //폼값 받는 컨트롤러 역할의 필터

    private final AuthenticationManager authenticationManager;

    private final JWTUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        //1. 클라이언트 로그인 요청시 id, password 받아서 출력
        String username = super.obtainUsername(request); // id
        String password = super.obtainPassword(request); // password

        log.info("username = {}", username); // 뭔지 찍어보자
        log.info("password = {}", password); // 뭔지 찍어보자

        // 2. 스프링 시큐러티에서는 username, password를 검증하기 위해서 ~~token에 담는다.
        // 지금은 authorization은 없어서  null 로 담았다. // 근데 나중에는 있을라나...?
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(username, password, null);

        // 3. token을 ~ Manager에 전달...Provoder...DetailsServicve...db연결...CustomMemberDetails생성..Back/Back/...
        Authentication authentication = authenticationManager.authenticate(authToken); // CustomMemberDetails정보를 반환...
        log.info("authentication = {}", authentication);
        return authentication;
    }

    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException {

        response.setContentType("text/html;charset=UTF-8");
        log.info("로그인 성공 ......");

        // UserDetails
        CustomMemberDetails customMemberDetails = (CustomMemberDetails) authentication.getPrincipal();

        /*
        하나의 유저가 여러개의 권한을 가질수 있기 때문에 collection으로 반환됨
        기본 제네릭이 GrantedAuthority이고 GrantedAuthority를 상속받은 자식들이 Role 이 된다
        이렇게 해서 Role을 만들어준다... MemberServiceImpl signUp에서 Role Setting(member.setRole("ROLE_USER"))
        우리는 하나의 권한만 지정했다.. ROLE_USER
        */
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();

        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority(); //ROLE_USER or  ROLE_ADMIN

        // 토큰생성과정...이때 password는 JWTUtil에서 안담았다.
        // 왜 안담았는가...ㅠ

        String token = jwtUtil.createJwt(
                customMemberDetails.getManagementUser(), role, 1000L * 60 * 10L);// 1초*60*10 10분

        System.out.println("token = " + token);
        System.out.println("@@@@@@@@@@@@@@@@@@ getMember " + customMemberDetails.getManagementUser() + " @@@@@@@@@@@@@@@@@@");

        // 응답할 헤더를 설정
        // 베어러 뒤에 공백을 준다. 관례적인  prefix
        // 결론은 그냥이라는 뜻이네
        response.addHeader("Authorization", "Bearer " + token);

        Map<String, Object> map = new HashMap<>();

            ManagementUser managementUser = customMemberDetails.getManagementUser();
            map.put("userNo", managementUser.getUserSeq()); // 시퀀스
            map.put("id", managementUser.getId()); // 아이디
            map.put("content", managementUser.getContent()); // 테이블명

        Gson gson = new Gson();
        String arr = gson.toJson(map);
        response.getWriter().print(arr);
    }

    //로그인 실패시 실행하는 메소드
    //CustomMemberDetailsService에서 null이 떨어지면 이곳으로 리턴..
    //응답 메세지를 Json형태로 프론트 단으로 넘기기 위해서 Gson 라이브러리 사용함.
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {

        response.setContentType("text/html;charset=UTF-8");

        log.info("로그인 실패... ......");
        //로그인 실패시 401 응답 코드 반환
        response.setStatus(401);

        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", "정보를 다시 확인해주세요.");
        Gson gson = new Gson();
        String arr = gson.toJson(map);
        response.getWriter().print(arr);
    }
}