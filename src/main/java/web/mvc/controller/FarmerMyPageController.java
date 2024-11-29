package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.mvc.domain.FarmerUser;
import web.mvc.dto.*;
import web.mvc.service.FarmerMyPageService;

import java.util.List;

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
    public ResponseEntity<List<UserBuyDTO>> saleList(@PathVariable Long seq) {
        List<UserBuyDTO> list = farmerMyPageService.saleList(seq);
        log.info("UserBuyDTO = {}", list);
        return ResponseEntity.ok(list);
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
     * 정산 내역
     */
    @GetMapping("/point/calc/{seq}")
    public ResponseEntity<?> calcPoint(@PathVariable Long seq) {
        return new ResponseEntity<>(farmerMyPageService.calcPoint(seq), HttpStatus.OK);
    }

    /**
     * 나한테 쓴 리뷰 조회하기
     */
    @GetMapping("/review/List/{seq}")
    public ResponseEntity<?> reviewList(@PathVariable Long seq) {
        return new ResponseEntity<>(farmerMyPageService.reviewList(seq), HttpStatus.OK);
    }

    /**
     * 정산 신청하기
     */
    @PostMapping("/settle/{seq}")
    public void settle(@PathVariable Long seq, @RequestBody CalculateDTO calculateDTO) {
        List<CalcPoint2> list = calculateDTO.getCalculateDTO();
        log.info("정산 신청 정보들 = {}", list);
        farmerMyPageService.settle(seq, list);
    }
}