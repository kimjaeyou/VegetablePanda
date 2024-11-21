package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.mvc.domain.CompanyUser;
import web.mvc.domain.FarmerUser;
import web.mvc.dto.CalcPoint;
import web.mvc.dto.UserBuyDTO;
import web.mvc.service.FarmerMyPageService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/myPage/farmer")
public class FarmerMyPageController {

    private final FarmerMyPageService farmerMyPageService;

    @GetMapping("")
    public String test() {
        log.info("판매자 마이페이지 test");
        return "판매자 마이페이지";
    }
    /**
     * 판매내역
     */
    @GetMapping("/saleList/{seq}")
    public String buyList(@PathVariable Long seq, Model model) {
        log.info("주문내역 조회시작");

        List<UserBuyDTO> list = farmerMyPageService.buyList(seq);
        model.addAttribute("list", list);
        log.info("list = {}", list);
        return "redirect:/buyList/" + seq;
    }

    /**
     * 회원의 정보 조회
     * 일단 값은 들고와야하니까..
     */
    @GetMapping("/list/{seq}")
    public String selectUser(@PathVariable Long seq, Model model) {
        FarmerUser farmerUser = farmerMyPageService.selectUser(seq);
        log.info("farmerUser = {}", farmerUser);
        model.addAttribute("farmerUser", farmerUser);
        return "redirect:/list/" + seq;
    }

    /**
     * 회원정보 수정
     */
    @PostMapping("/update/{seq}")
    public String update(@RequestBody FarmerUser farmerUser, @PathVariable Long seq) {
        farmerMyPageService.update(farmerUser, seq);
        return "redirect:/update";
    }

    /**
     * 탈퇴
     * 사실상 말이 탈퇴지
     * 그냥 계정 정지임. 그럼 상태값을 바꿔주기만 하면될듯.
     * 근데 이거 Post로 해도 되나...?
     */
    @PostMapping("/delete/{seq}")
    public String delete(@PathVariable Long seq) {
        farmerMyPageService.delete(seq);
        return "redirect:/main"; // 이건 아직 안넣었음, 왜냐면 이거 탈퇴하면 메인페이지로 갈라고, 메인페이지 url몰라...그래서 일단 main이라고만 적어두자
    }

    /**
     * 정산 신청
     * 이건 뭐 어떻게 해줘야할까...
     * 그냥 신청서처럼 해줘야하나...
     */
    @PostMapping("/calcPoint/{seq}")
    public String calcPoint(@PathVariable Long seq, @RequestBody UserBuyDTO userBuyDTO) {
        farmerMyPageService.calcPoint(seq, userBuyDTO);
        return "/calcPoint/" + seq;
    }
}