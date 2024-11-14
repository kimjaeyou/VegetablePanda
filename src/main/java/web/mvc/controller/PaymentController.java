package web.mvc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import web.mvc.dto.UserBuyReq;
import web.mvc.service.PaymentService;

import java.util.Map;

@RestController("/order/payment")
@Slf4j
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    // 결제
    public Map<Object, Object> paymentInsert(UserBuyReq userBuyReq) {
        // 주문상품 seq, 주문 유저 seq, 상태값?, 구매 수량, 총 주문 가격, 할인율
        // 경매 상품 결제
        log.info("paymentInsert 호출");

        // UserBuy 정보 insert
        paymentService.paymentInsert(userBuyReq);


        return null;
    }
}
