package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final CompanyMyPageService companyMyPageService;

    /**
     * 일단 페이지가 잘 만들어 졌는지 테스트
     */
    @GetMapping("/")
    public ResponseEntity<String> test() {
        log.info("일반 마이페이지 test");
        return ResponseEntity.ok("일반 마이페이지");
    }

    /**
     * 판매내역 조회
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
    public ResponseEntity<UserDTO> selectUser(@PathVariable Long seq) {
        UserDTO userDTO = userMyPageService.selectUser(seq);
        log.info("userDTO = {}", userDTO);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/list/company/{seq}")
    public ResponseEntity<CompanyDTO> selectCompany(@PathVariable Long seq) {
        CompanyDTO companyDTO = companyMyPageService.selectCompany(seq);
        log.info("companyDTO = {}", companyDTO);
        return ResponseEntity.ok(companyDTO);
    }

    /**
     * 회원정보 수정
     */
    @PostMapping("/update/{seq}")
    public ResponseEntity<String> update(@RequestBody UserUpdateDTO userUpdateDTO, @PathVariable Long seq) {
        userMyPageService.update(userUpdateDTO, seq);
        return ResponseEntity.ok("회원정보 수정 완료");
    }

    /**
     * 탈퇴 (계정 상태 변경)
     */
    @PostMapping("/delete/{seq}")
    public ResponseEntity<String> delete(@PathVariable Long seq) {
        userMyPageService.delete(seq);
        return ResponseEntity.ok("계정 정지 완료");
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