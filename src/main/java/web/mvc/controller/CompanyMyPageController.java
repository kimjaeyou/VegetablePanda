package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.mvc.domain.Bid;
import web.mvc.domain.CompanyUser;
import web.mvc.domain.ReviewComment;
import web.mvc.dto.ReviewCommentDTO;
import web.mvc.dto.UserBuyDTO;
import web.mvc.service.CompanyMyPageService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/myPage/company")
public class CompanyMyPageController {

    private final CompanyMyPageService companyMyPageService;

    /**
     * 회원의 정보 조회
     */
    @GetMapping("/list/{seq}")
    public ResponseEntity<?> selectUser(@PathVariable Long seq) {
        return new ResponseEntity<>(companyMyPageService.selectUser(seq), HttpStatus.OK);
    }

    /**
     * 회원정보 수정
     */
    @PutMapping("/update/{seq}")
    public ResponseEntity<?> update(
            @PathVariable Long seq,
            @RequestParam String comName,
            @RequestParam String ownerName,
            @RequestParam String regName,
            @RequestParam String email,
            @RequestParam String code,
            @RequestParam String address,
            @RequestParam String phone,
            @RequestParam String pw
    ) {

        CompanyUser companyUser = new CompanyUser();
        companyUser.setUserSeq(seq);
        companyUser.setComName(comName);
        companyUser.setOwnerName(ownerName);
        companyUser.setRegName(regName);
        companyUser.setEmail(email);
        companyUser.setCode(code);
        companyUser.setAddress(address);
        companyUser.setPhone(phone);
        companyUser.setPw(pw);

        return new ResponseEntity<>( companyMyPageService.update(companyUser, seq), HttpStatus.OK);
    }
    /**
     * 탈퇴 (계정 상태 변경)
     */
    @PutMapping("/delete/{seq}")
    public int delete(@PathVariable Long seq) {
        return companyMyPageService.delete(seq);
    }

}