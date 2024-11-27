package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.mvc.domain.CompanyUser;
import web.mvc.domain.FarmerUser;
import web.mvc.domain.ReviewComment;
import web.mvc.domain.User;
import web.mvc.dto.*;
import web.mvc.service.FarmerMyPageService;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/myPage/farmer")
public class FarmerMyPageController {

    private final FarmerMyPageService farmerMyPageService;

    /**
     * 판매내역
     */
    @GetMapping("/saleList/{seq}")
    public String buyList(@PathVariable Long seq, Model model) {
        log.info("판매내역 조회시작");

        List<UserBuyDTO> list = farmerMyPageService.buyList(seq);
        model.addAttribute("list", list);
        log.info("list = {}", list);
        return "redirect:/,saleList/" + seq;
    }

    /**
     * 회원의 정보 조회
     */
    @GetMapping("/list/{seq}")
    public ResponseEntity<?> selectUser(@PathVariable Long seq) {
        FarmerUser farmerUser = farmerMyPageService.selectUser(seq);

        FarmerUserDTO farmerUserDTO = new FarmerUserDTO();
        farmerUserDTO.setFarmerId(farmerUser.getFarmerId());
        farmerUserDTO.setName(farmerUser.getName());
        farmerUserDTO.setCode(farmerUser.getCode());
        farmerUserDTO.setAddress(farmerUser.getAddress());
        farmerUserDTO.setPw(farmerUser.getPw());
        farmerUserDTO.setPhone(farmerUser.getFarmerId());
        farmerUserDTO.setEmail(farmerUser.getEmail());
        farmerUserDTO.setGrade(String.valueOf(farmerUser.getFarmerGrade()));
        farmerUserDTO.setRegDate(String.valueOf(farmerUser.getRegDate()));

        return new ResponseEntity<>(farmerUserDTO, HttpStatus.OK);
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
            @RequestParam String code) {

        FarmerUser farmerUser = new FarmerUser();
        farmerUser.setUserSeq(seq);
        farmerUser.setName(name);
        farmerUser.setPw(pw);
        farmerUser.setAddress(address);
        farmerUser.setPhone(phone);
        farmerUser.setEmail(email);
        farmerUser.setCode(code);

        return new ResponseEntity<>( farmerMyPageService.update(farmerUser, seq), HttpStatus.OK);
    }

    /**
     * 탈퇴
     */
    @PostMapping("/delete/{seq}")
    public int delete(@PathVariable Long seq) {
        return farmerMyPageService.delete(seq);
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

    /**
     * 나한테 쓴 리뷰 조회하기
     */
    @GetMapping("review/List/{seq}")
    public ResponseEntity<?> reviewList(@PathVariable Long reviewSeq) {
        List<ReviewCommentDTO> reviewComments = farmerMyPageService.reviewList(reviewSeq);

        return new ResponseEntity<>(reviewComments, HttpStatus.OK);
    }
}