package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.mvc.domain.Bid;
import web.mvc.domain.ReviewComment;
import web.mvc.domain.User;
import web.mvc.dto.*;
import web.mvc.service.CompanyMyPageService;
import web.mvc.service.UserMyPageService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/myPage")
public class UserMyPageController {

    private final UserMyPageService userMyPageService;

    /**
     * 주문내역 조회
     */
    @GetMapping("/buyList/{seq}")
    public ResponseEntity<List<UserBuyDTO>> buyList(@PathVariable Long seq) {
        log.info("주문내역 조회시작");
        List<UserBuyDTO> list = userMyPageService.buyList(seq);
        log.info("list = {}", list);
        return ResponseEntity.ok(list);
    }

    /**
     * 회원의 정보 조회
     */
    @GetMapping("/list/{seq}")
    public ResponseEntity<?> selectUser(@PathVariable Long seq) {
        return new ResponseEntity<>(userMyPageService.selectUser(seq), HttpStatus.OK);
    }

    /**
     * 회원정보 수정
     */
    @PutMapping("/update/{seq}")
    public ResponseEntity<?> update(
            @PathVariable Long seq,
            @RequestParam String name,
            @RequestParam String pw,
            @RequestParam String address,
            @RequestParam String phone,
            @RequestParam String email,
            @RequestParam String gender) {

        User user = new User();
        user.setUserSeq(seq);
        user.setName(name);
        user.setPw(pw);
        user.setAddress(address);
        user.setPhone(phone);
        user.setEmail(email);
        user.setGender(gender);

        return new ResponseEntity<>( userMyPageService.update(user, seq), HttpStatus.OK);
    }

    /**
     * 탈퇴 (계정 상태 변경)
     */
    @PostMapping("/delete/{seq}")
    public int delete(@PathVariable Long seq) {
        return userMyPageService.delete(seq);
    }


    /**
     * 지갑 잔액 조회
     */
    @GetMapping("/point/{seq}")
    public ResponseEntity<Integer> point(@PathVariable Long seq) {
        int point = userMyPageService.point(seq);
        log.info("point = {}", point);
        return ResponseEntity.ok(point);
    }

    /**
     * 나의 활동내역 리스트 - 리뷰 조회
     */
    @GetMapping("/review/{seq}")
    public ResponseEntity<List<ReviewCommentDTO>> review(@PathVariable Long seq) {
        List<ReviewCommentDTO> list = userMyPageService.review(seq);
        log.info("list = {}", list);
        return ResponseEntity.ok(list);
    }

    /**
     * 리뷰 삭제
     */
    @DeleteMapping("/review/{userSeq}/{reviewSeq}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewSeq, @PathVariable Long userSeq) {
        userMyPageService.deleteReview(reviewSeq, userSeq);
        return ResponseEntity.ok("리뷰 삭제 완료");
    }

    /**
     * 경매 참여내역 조회
     */
    @GetMapping("/auction/{seq}")
    public ResponseEntity<List<Bid>> auctionList(@PathVariable Long seq) {
        List<Bid> list = userMyPageService.auctionList(seq);
        return ResponseEntity.ok(list);
    }
}