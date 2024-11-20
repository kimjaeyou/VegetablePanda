package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class FarmerMyPageController {


    @GetMapping("/farmer/myPage")
    public String test() {
        log.info("판매자 마이페이지 test");
        return "test";
    }
    /**
     * 판매내역
     */

    /**
     * 물류 관리
     * 일단 처음엔 내가 등록한 물류들 리스트 출력
     * 부수 기능 : 등록 , 수정, 삭제
     */

    /**
     * 회원정보 수정
     */

    /**
     * 탈퇴
     */

    /**
     * 정산 신청
     */


}