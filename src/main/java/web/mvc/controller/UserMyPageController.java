package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.mvc.domain.Bid;
import web.mvc.domain.ReviewComment;
import web.mvc.domain.User;
import web.mvc.dto.AuctionDTO2;
import web.mvc.dto.UserBuyDTO;
import web.mvc.service.UserMyPageService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/myPage/user")
public class UserMyPageController {

    private final UserMyPageService userMyPageService;

    /**
     * 일단 페이지가 잘 만들어 졌는지 테스트
     * 성공
     */
    @GetMapping("")
    public String test() {
        log.info("일반 마이페이지 test");
        return "일반 마이페이지";
    }

    /**
     * 주문내역
     * 화면에 해당되는 아이디의 주문내역을 출력해주기.
     * 성공
     */
    @GetMapping("/buyList/{seq}")
    public String buyList(@PathVariable Long seq, Model model) {
        log.info("주문내역 조회시작");
        // 토큰값에서 시퀀스 꺼내서 그 시퀀스를 들고 가야하는데... 어떻게 하지...

        List<UserBuyDTO> list = userMyPageService.buyList(seq);
        model.addAttribute("list", list);
        log.info("list = {}", list);
        return "/buyList/" + seq;
    }

    /**
     * 회원의 정보 조회
     * 일단 값은 들고와야하니까..
     * 얘도 성공
     */
    @GetMapping("/list/{seq}")
    public String selectUser(@PathVariable Long seq, Model model) {
        User user = userMyPageService.selectUser(seq);
        log.info("user = {}", user.getId());
        model.addAttribute("userInfo", user);
        return "/list/" + seq;
    }

    /**
     * 회원정보 수정
     * 성공
     */
    @PostMapping("/update/{seq}")
    public String update(@RequestBody User user, @PathVariable Long seq) {
        userMyPageService.update(user, seq);
        return "redirect:/list";
    }

    /**
     * 탈퇴
     * 사실상 말이 탈퇴지
     * 그냥 계정 정지임. 그럼 상태값을 바꿔주기만 하면될듯.
     * 근데 이거 Post로 해도 되나...?
     * 성공
     */
    @PostMapping("/delete/{seq}")
    public String delete(@PathVariable Long seq) {
        userMyPageService.delete(seq);
        return "redirect:/main"; // 이건 아직 안넣었음, 왜냐면 이거 탈퇴하면 메인페이지로 갈라고, 메인페이지 url몰라...그래서 일단 main이라고만 적어두자
    }

    /**
     * 지갑 잔액조회
     * 충전은 인영님이 결제 API로 넣어주신다고 하셨으니까 일단 금액만 조회 되게 해보자
     * 성공
     */
    @PostMapping("/point/{seq}")
    public String point(@PathVariable Long seq, Model model) {
        int point = userMyPageService.point(seq);
        log.info("point = {}", point);
        model.addAttribute("point", point);
        return "redirect:/user/point";
    }

    // 여기 두개는 어차피 똑같은 코드일테니까 일단 위에부터 해결해보자...
    // 17:35 : 이거 먼저 해야되네...

    /**
     * 나의 활동내역 리스트
     * 리뷰 조회
     * 성공
     */
    @GetMapping("/review/{seq}")
    public String review(@PathVariable Long seq, Model model) {
        List<ReviewComment> list = userMyPageService.review(seq);
        model.addAttribute("list", list);
        log.info("list = {}", list);
        return "/review/" + seq;
    }

    /**
     * 나의 활동내역 리스트
     * 일단 리뷰 삭제
     * 나중에 뭐 자유게시판 나오면 또 하겠씁니다.
     */
    @PostMapping("/review/delete/{userSeq}/{reviewSeq}")
    public String deleteReview(@PathVariable Long reviewSeq, @PathVariable Long userSeq) {
        userMyPageService.deleteReview(reviewSeq, userSeq);
        return "/review/delete/" + userSeq + reviewSeq;
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
    @GetMapping("/auctionList/{seq}")
    public String auctionList(@PathVariable Long seq, Model model) {
        List<AuctionDTO2> list = userMyPageService.auctionList(seq);
        model.addAttribute("list", list);
        return "/auction/" + seq;
    }
}