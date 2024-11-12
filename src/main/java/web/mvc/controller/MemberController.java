package web.mvc.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import web.mvc.domain.Member;
import web.mvc.service.MemberService;

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
    public String signUp(@RequestBody Member member) {
        memberService.signUp(member);
        return "ok";
    }
}
