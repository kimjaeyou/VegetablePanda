package web.mvc.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import web.mvc.dto.GetAllUserDTO;
import web.mvc.security.CustomMemberDetails;
import web.mvc.service.MemberService;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@Slf4j
@Tag(name = "MemberController API", description = "Security Swagger 테스트용  API")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/test")
    public String test() {
        log.info("test 요청");
        return "test";
    }

    @GetMapping("/members/{id}")
    public String duplicateIdCheck(@PathVariable String id) {
        log.info("id={}",id);
        return memberService.duplicateCheck(id);
    }

    @PostMapping("/members")
    public String signUp(@RequestBody GetAllUserDTO user) {
        memberService.signUp(user);
        return "ok";
    }
    //추가한 부분
    @GetMapping("/api/user")
    public ResponseEntity<?> getUserInfo() {
        CustomMemberDetails userDetails =
                (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Map<String, Object> response = new HashMap<>();
        response.put("id", userDetails.getUser().getId());
        response.put("name", userDetails.getUser().getName());
        response.put("role", userDetails.getUser().getRole());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        // 현재 SecurityContext에서 인증 정보 제거
        SecurityContextHolder.clearContext();

        // 세션 무효화
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // 클라이언트 쿠키 삭제
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // HTTPS에서만 전달
        cookie.setMaxAge(0); // 쿠키 즉시 삭제
        cookie.setPath("/");
        response.addCookie(cookie);

        log.info("사용자 로그아웃 성공");
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

}