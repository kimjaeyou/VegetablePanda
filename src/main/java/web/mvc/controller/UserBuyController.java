package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import web.mvc.domain.ManagementUser;
import web.mvc.domain.UserBuy;
import web.mvc.domain.UserBuyDetail;
import web.mvc.domain.UserCharge;
import web.mvc.dto.UserBuyReq;
import web.mvc.dto.UserBuyRes;
import web.mvc.dto.UserChargeDTO;
import web.mvc.service.UserBuyDetailService;
import web.mvc.service.UserBuyService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserBuyController {

    private final UserBuyService userBuyService;
//    private final UserBuyDetailService userBuyDetailService;
    private final ModelMapper modelMapper;

    // 일반 물품 즉시 구매하기
    @PostMapping("/shop/purchase")
    public ResponseEntity<?> charge(@RequestBody UserBuyReq userBuyReq) {
        log.info("UserChargeController!!");
        log.info("UserBuyReq: {}", userBuyReq);
        // 주문번호 생성
//        String orderUid = userChargeService.generateOrderUid();
//        userChargeDTO.setOrderUid(orderUid);
//        log.info("orderUid={}", orderUid);
        //UserCharge userCharge = userChargeDTO.toUserCharge(userChargeDTO);

        UserBuy userBuy = modelMapper.map(userBuyReq, UserBuy.class);
        userBuy.setManagementUser(ManagementUser.builder().userSeq(userBuyReq.getUserSeq()).build());

        log.info("UserBuy의 user_seq: {}", userBuy.getManagementUser().getUserSeq());
        System.out.println(userBuy);

        UserBuyRes result = modelMapper.map(userBuyService.insertShopOrder(userBuy), UserBuyRes.class);
        //return new ResponseEntity<>(result, HttpStatus.OK);
        System.out.println("주문번호 = " + result.getBuySeq());
        return ResponseEntity.ok(result.getBuySeq());
    }
}
