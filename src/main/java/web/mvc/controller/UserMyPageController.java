package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import web.mvc.domain.Bid;
import web.mvc.domain.ReviewComment;
import web.mvc.dto.UserBuyDTO;
import web.mvc.dto.UserDTO;
import web.mvc.service.UserMyPageService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class UserMyPageController {

    private UserMyPageService userMyPageService;

    /**
     * 일단 페이지가 잘 만들어 졌는지 테스트
     */
    @GetMapping("/user/myPage")
    public String test() {
        log.info("일반 마이페이지 test");
        return "일반 마이페이지";
    }

    /**
     * 주문내역
     * 화면에 해당되는 아이디의 주문내역을 출력해주기.
     */
    @GetMapping("/user/buyList/{seq}")
    public String buyList(@PathVariable Long seq, Model model) {
        log.info("주문내역 조회시작");
        // 토큰값에서 시퀀스 꺼내서 그 시퀀스를 들고 가야하는데... 어떻게 하지...

        List<UserBuyDTO> list = userMyPageService.buyList(seq);
        model.addAttribute("list", list);
        return "/user/buyList/" + seq;
    }

    /**
     * 회원의 정보 조회
     * 일단 값은 들고와야하니까..
     */
    @GetMapping("/user/list/{seq}")
    public ModelAndView selectUser(@PathVariable Long seq, Model model) {
        UserDTO userDTO = userMyPageService.selectUser(seq);
        return new ModelAndView("user/list" + seq, "user", userDTO);
    }

    /**
     * 회원정보 수정
     */
    @PostMapping("/user/update")
    public String update(UserDTO userDTO) {
        userMyPageService.update(userDTO);
        return "redirect:/user/update";
    }

    /**
     * 탈퇴
     * 사실상 말이 탈퇴지
     * 그냥 계정 정지임. 그럼 상태값을 바꿔주기만 하면될듯.
     * 근데 이거 Post로 해도 되나...?
     */
    @PostMapping("/user/delete")
    public String delete(@RequestParam int state) {
        userMyPageService.delete(state);
        return ""; // 이건 아직 안넣었음, 왜냐면 이거 탈퇴하면 메인페이지로 갈라고, 메인페이지 url몰라...
    }

    /**
     * 지갑 잔액조회
     * 충전은 인영님이 결제 API로 넣어주신다고 하셨으니까 일단 조회만 되게 해보자
     */
    @PostMapping("/user/point")
    public String point(Long seq, Model model) {
        int point = userMyPageService.point(seq);
        model.addAttribute("point", point);
        return "redirect:/user/point";
    }

    // 여기 두개는 어차피 똑같은 코드일테니까 일단 위에부터 해결해보자...
    // 17:35 : 이거 먼저 해야되네...

    /**
     * 나의 활동내역 리스트
     * 리뷰 조회
     */
    @GetMapping("/user/review/{seq}")
    public String review(@PathVariable Long seq, Model model) {
        List<ReviewComment> list = userMyPageService.review(seq);
        model.addAttribute("list", list);
        return "/user/review/" + seq;
    }

    /**
     * 나의 활동내역 리스트
     * 리뷰 삭제
     */
    @PostMapping("/user/review/delete/{seq}")
    public String deleteReview(@PathVariable Long seq) {
        userMyPageService.deleteReview(seq);
        return "/user/review/delete/"+seq;
    }

    /**
     * 나의 활동내역 리스트
     * 경매참여내역
     * // 모르겠다..
     */
    @GetMapping("/user/auction/{seq}")
    public String auctionList(@PathVariable Long seq, Model model) {
        List<Bid> list = userMyPageService.auctionList(seq);
        model.addAttribute("list", list);
        return "/user/auction/" + seq;
    }

}