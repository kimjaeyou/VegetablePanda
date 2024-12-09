package web.mvc.controller;

import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.mvc.domain.UserBuy;
import web.mvc.domain.UserBuyDetail;
import web.mvc.domain.UserCharge;
import web.mvc.dto.PaymentReq;
import web.mvc.dto.RequestPayDTO;
import web.mvc.dto.UserBuyReq;
import web.mvc.dto.UserChargeDTO;
import web.mvc.exception.ErrorCode;
import web.mvc.exception.UserBuyException;
import web.mvc.service.PaymentService;
import web.mvc.service.UserBuyService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController()
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final UserBuyService userBuyService;
    private final ModelMapper modelMapper;

    // API 결제 : 주문 객체를 받아서 결제 진행
    public Map<Object, Object> paymentInsert(UserBuyReq userBuyReq) {
        // 주문 seq, 주문 유저 seq, 상태값, 총 주문 가격 : 주문정보
        // 주문 상품 리스트, 상품명, 가격, 수량, 할인율 : 주문 품목 정보
        // 경매 상품 선금 결제 : 경매 물품명(name:물품명+선금), 가격, 수량
        // 경매 상품 잔금 결제 : 경매 물품명(name:물품명+잔금)

        log.info("paymentInsert 호출");

        UserBuy userBuy = modelMapper.map(userBuyReq, UserBuy.class);

        // UserBuy 정보 insert
        paymentService.paymentInsert(userBuy);


        return null;
    }

    // 포인트 충전 // 충전 정보 임시로 DB에 넣은 후 프론트에서 결제 진행, 검증 후 DB에 포인트 충전 진행
    //@PostMapping("/payment/charge")
    public Map<Object, Object> chargePoint(@RequestBody UserChargeDTO userChargeDTO) {
        // 충전 날짜
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = format.format(new Date());
        System.out.println("충전일자 : " + date);
        userChargeDTO.setChargeDate(date);

        UserCharge userCharge = modelMapper.map(userChargeDTO, UserCharge.class);
        int result = paymentService.saveCharge(userCharge);
        Map<Object, Object> map = new HashMap<>();
        if(result > 0){
            map.put("cnt", 1);
            UserCharge charge = paymentService.getLastCharge();
            map.put("userChargeSeq", charge.getUserChargeSeq());
            map.put("chargePrice", charge.getPrice());
            map.put("chargeDate", charge.getChargeDate());
        } else {
            map.put("cnt", 0);
            map.put("msg", "충전에 실패했습니다. 다시 시도해주세요.");
        }

        return map;
    }

    // 주문내역에서 결제정보 찾아와 넘겨주기
    @GetMapping("/{id}")
    public ResponseEntity<?> paymentPage(@PathVariable("id") String orderUid, int status) {
        // 재고수량 체크
        if(status == 2) {
            this.checkStockCount(orderUid);
        }

        // 결제정보 불러오기
        log.info("paymentPage");
        RequestPayDTO requestDTO = paymentService.findRequestDto(orderUid, status);
        ResponseEntity<?> entity = new ResponseEntity(requestDTO, HttpStatus.OK);
        return entity;
    }

    // 결제 검증
    @PostMapping("/validate")
    public ResponseEntity<IamportResponse<Payment>> validatePayment(@RequestBody PaymentReq paymentReq, int status) {
        IamportResponse<Payment> iamportResponse = null;
        if(status == 1) {
            iamportResponse = paymentService.paymentByChargeCallback(paymentReq);
        } else {
            log.info("validatePayment status 2 입니다.");
            iamportResponse = paymentService.paymentByCallback(paymentReq);

        }

        log.info("결제 응답 : {}", iamportResponse.getResponse().toString());
        return new ResponseEntity<>(iamportResponse, HttpStatus.OK);
        // 유저 wallet 정보 or 결제정보 반환
    }

    // 일반상품 구매 재고수량 확인
    public void checkStockCount (String orderUid) {
        log.info("일반상품 구매의 경우 재고 수량 확인");
        UserBuy userBuy = userBuyService.findByOrderUid(orderUid);

        List<UserBuyDetail> detailList = userBuy.getUserBuyDetailList();
        for(UserBuyDetail detail : detailList){
            int stockCount = detail.getStock().getCount();
            if(stockCount < detail.getCount()){
                throw new UserBuyException(ErrorCode.ORDER_FAILED);
            }
        }
    }

}
