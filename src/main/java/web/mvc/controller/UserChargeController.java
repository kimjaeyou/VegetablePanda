package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import web.mvc.domain.UserCharge;
import web.mvc.domain.UserWallet;
import web.mvc.dto.UserChargeDTO;
import web.mvc.dto.UserWalletDTO;
import web.mvc.service.UserChargeService;

import java.util.Date;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserChargeController {

    private final UserChargeService userChargeService;
    private final ModelMapper modelMapper;

    // 포인트 충전 주문하기
    @PostMapping("/charge")
    public String charge(@RequestBody UserChargeDTO userChargeDTO) {
        log.info("UserChargeController!!");

        // 주문번호 생성
        String orderUid = userChargeService.generateOrderUid();
        userChargeDTO.setOrderUid(orderUid);
        log.info(orderUid);
        //UserCharge userCharge = userChargeDTO.toUserCharge(userChargeDTO);
        UserCharge usercharge = modelMapper.map(userChargeDTO, UserCharge.class);
        System.out.println(usercharge);

        UserChargeDTO result = modelMapper.map(userChargeService.order(usercharge), UserChargeDTO.class);
        //return new ResponseEntity<>(result, HttpStatus.OK);
        return "redirect:/payment/" + orderUid;
    }

    // 결제 검증까지 완료된 후 결제 금액만큼 포인트 충전
    @GetMapping("/charge/point")
    public ResponseEntity<?> chargePointComplete (int point, long userSeq) {
        log.info("지갑에 금액 충전");
        UserWallet wallet = userChargeService.chargeWallet(point, userSeq);
        UserWalletDTO walletDTO = modelMapper.map(wallet, UserWalletDTO.class);
//        return wallet.getPoint()+"포인트 충전 성공";
        return new ResponseEntity<>(walletDTO, HttpStatus.OK);
    }


}
