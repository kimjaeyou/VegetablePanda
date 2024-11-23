package web.mvc.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
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
        log.info("user={}",user);
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
}