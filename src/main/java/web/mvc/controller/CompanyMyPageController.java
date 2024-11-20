package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.mvc.domain.Bid;
import web.mvc.domain.CompanyUser;
import web.mvc.domain.ReviewComment;
import web.mvc.dto.UserBuyDTO;
import web.mvc.service.CompanyMyPageService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class CompanyMyPageController {

    private final CompanyMyPageService companyMyPageService;

    /**
     * 일단 페이지가 잘 만들어 졌는지 테스트
     * 근데 이건 왜 안되냐
     */
    @GetMapping("/company/myPage")
    public String test() {
        log.info("업체 마이페이지 test");
        return "업체 마이페이지";
    }

    /**
     * 주문내역
     * 화면에 해당되는 아이디의 주문내역을 출력해주기.
     */
    @GetMapping("/company/buyList/{seq}")
    public String buyList(@PathVariable Long seq, Model model) {
        log.info("주문내역 조회시작");
        // 토큰값에서 시퀀스 꺼내서 그 시퀀스를 들고 가야하는데... 어떻게 하지...

        List<UserBuyDTO> list = companyMyPageService.buyList(seq);
        model.addAttribute("list", list);
        log.info("list = {}", list);
        return "/company/buyList/" + seq;
    }

    /**
     * 회원의 정보 조회
     * 일단 값은 들고와야하니까..
     */
    @GetMapping("/company/list/{seq}")
    public String selectUser(@PathVariable Long seq, Model model) {
        CompanyUser companyUser = companyMyPageService.selectUser(seq);
        log.info("companyUser = {}", companyUser.getCompanyId());
        model.addAttribute("companyUser", companyUser);
        return "/CompanyUser/list/" + seq;
    }

    /**
     * 회원정보 수정
     */
    @PostMapping("/company/update/{seq}")
    public String update(@RequestBody CompanyUser companyUser, @PathVariable Long seq) {
        companyMyPageService.update(companyUser, seq);
        return "redirect:/company/update";
    }

    /**
     * 탈퇴
     * 사실상 말이 탈퇴지
     * 그냥 계정 정지임. 그럼 상태값을 바꿔주기만 하면될듯.
     * 근데 이거 Post로 해도 되나...?
     */
    @PostMapping("/company/delete/{seq}")
    public String delete(@PathVariable Long seq) {
        companyMyPageService.delete(seq);
        return "redirect:/main"; // 이건 아직 안넣었음, 왜냐면 이거 탈퇴하면 메인페이지로 갈라고, 메인페이지 url몰라...그래서 일단 main이라고만 적어두자
    }

    /**
     * 지갑 잔액조회
     * 충전은 인영님이 결제 API로 넣어주신다고 하셨으니까 일단 조회만 되게 해보자
     */
    @PostMapping("/company/point/{seq}")
    public String point(@PathVariable Long seq, Model model) {
        int point = companyMyPageService.point(seq);
        log.info("point = {}", point);
        model.addAttribute("point", point);
        return "redirect:/user/point";
    }


    /**
     * 나의 활동내역 리스트
     * 리뷰 조회
     */
    @GetMapping("/company/review/{seq}")
    public String review(@PathVariable Long seq, Model model) {
        List<ReviewComment> list = companyMyPageService.review(seq);
        model.addAttribute("list", list);
        log.info("list = {}", list);
        return "/company/review/" + seq;
    }

    /**
     * 나의 활동내역 리스트
     * 일단 리뷰 삭제
     * 나중에 뭐 자유게시판 나오면 또 하겠씁니다.
     */
    @PostMapping("/company/review/delete/{userSeq}/{reviewSeq}")
    public String deleteReview(@PathVariable Long reviewSeq, @PathVariable Long userSeq) {
        companyMyPageService.deleteReview(reviewSeq, userSeq);
        return "/company/review/delete/" + userSeq + reviewSeq;
    }

    /**
     * 나의 활동내역 리스트
     * 경매참여내역
     * // 이건 모르겠다.. 누가 좀 도와줘요...
     * 왜모르는건가 : 아니 그니까 내가 말하는건
     * 이게 내역이니까 참여했던 모든 입찰기록을 다 가져와야 하는건가,
     * 아니면 입찰에 성공한 기록만 가져와야 하는가에 대한 의문
     * 누가 이거 보면 알려줘요
     * 18:09 : 이거 나중에 할게여... 너무 답이 안나와요
     * 테이블이 너무 엉켜있어요
     */
    @GetMapping("/company/auction/{seq}")
    public String auctionList(@PathVariable Long seq, Model model) {
        List<Bid> list = companyMyPageService.auctionList(seq);
        model.addAttribute("list", list);
        return "/company/auction/" + seq;
    }
}