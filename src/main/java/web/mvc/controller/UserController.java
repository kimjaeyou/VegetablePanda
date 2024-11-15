package web.mvc.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import web.mvc.domain.CompanyUser;
import web.mvc.domain.FarmerUser;
import web.mvc.domain.User;
import web.mvc.service.UserService;

import static web.mvc.domain.QUser.user;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/test")
    public String test() {
        log.info("test 요청");
        return "Spring Security Test";
    }

    /**
     * 아이디 중복체크
     */
    @GetMapping("/user/{id}")
    public String duplicate(@PathVariable String id) {
        log.info("아이디 중복체크");
        return userService.duplicateCheck(id);
    }
    /**
     * 일반 회원가입
     */
    @PostMapping("/user/signUp")
    public String userSignUp(@RequestBody User user) {
        log.info("일반유저 회원가입");
        userService.userSignUp(user);
        return "가입 축하";
    }
    /**
     * 업체 회원가입
     */
    @PostMapping("/company/signUp")
    public String companySignUp(@RequestBody CompanyUser companyUser) {
        log.info("업체 회원가입");
        userService.companySignUp(companyUser);

        return "가입 축하해";
    }
    /**
     * 판매자 회원가입
     */
    @PostMapping("/farmer/signUp")
    public String farmerSignUp(@RequestBody FarmerUser farmerUser) {
        log.info("판매자 회원가입");
        userService.farmerSignUp(farmerUser);

        return "가입 축하한다";
    }
}