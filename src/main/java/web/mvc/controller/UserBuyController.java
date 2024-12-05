package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.mvc.domain.*;
import web.mvc.dto.UserBuyDetailDTO;
import web.mvc.dto.UserBuyReq;
import web.mvc.dto.UserBuyRes;
import web.mvc.dto.UserChargeDTO;
import web.mvc.service.UserBuyDetailService;
import web.mvc.service.UserBuyService;
import web.mvc.service.UserChargeService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserBuyController {

    private final UserBuyService userBuyService;
    private final UserBuyDetailService userBuyDetailService;
    private final UserChargeService userChargeService;
    private final ModelMapper modelMapper;

    // 일반 물품 즉시 구매하기
    @PostMapping("/shop/purchase")
    public ResponseEntity<?> charge(@RequestBody UserBuyReq userBuyReq) {
        log.info("UserChargeController!!");
        log.info("UserBuyReq: {}", userBuyReq);
        log.info("UserBuyDetailDTOs {}", userBuyReq.getUserBuyDetailDTOs());

        // 주문번호 생성
        String orderUid = userChargeService.generateOrderUid();
        userBuyReq.setOrderUid(orderUid);

        UserBuy userBuy = modelMapper.map(userBuyReq, UserBuy.class);
        userBuy.setManagementUser(ManagementUser.builder().userSeq(userBuyReq.getUserSeq()).build());

        // 이 부분 데이터가 인 들어가는 것 같음
        userBuy.setUserBuyDetailList(userBuyReq.getUserBuyDetailDTOs().stream()
                .map(detail -> modelMapper.map(detail, UserBuyDetail.class))
                .collect(Collectors.toList()));

        log.info("UserBuy의 user_seq: {}", userBuy.getManagementUser().getUserSeq());
        System.out.println(userBuy);

        log.info("Userbuyreq의 userbuydetail 정보 {}", userBuyReq.getUserBuyDetailDTOs());

        // 주문 상세 품목 변수 dtoList
        List<UserBuyDetailDTO> dtoList = userBuyReq.getUserBuyDetailDTOs();

        // UserBuy 주문 넣기
        log.info("UserBuy 주문 넣기 전 기록");
        UserBuyRes result = modelMapper.map(userBuyService.insertShopOrder(userBuy), UserBuyRes.class);
        log.info("UserBuyRes 정보 : {}", result);

        // UserBuyDetail 주문 상세 품목 정보 넣기

//        List<UserBuyDetail> detailList = dtoList.stream().map(detail ->
//        {
//            detail.setBuySeq(result.getBuySeq());
//            log.info("detail의 buySeq : {}", detail.getBuySeq());
//            return modelMapper.map(detail, UserBuyDetail.class);
//        }).collect(Collectors.toList());

        List<UserBuyDetail> detailList = dtoList.stream().map(detail -> {
            return new UserBuyDetail(result.getBuySeq(), detail.getPrice(), detail.getCount(), detail.getStockSeq());
        }).collect(Collectors.toList());
        log.info("detailList 정보 : {}", detailList);

        List<UserBuyDetail> detailResult = userBuyDetailService.insertUserBuyDetail(detailList);
        //return new ResponseEntity<>(result, HttpStatus.OK);
        System.out.println("주문번호 = " + result.getOrderUid());
        return ResponseEntity.ok(result.getOrderUid());
    }

    // 결제 중 취소되는 경우 주문 내역 삭제
    @DeleteMapping("/shop/cancel")
    public ResponseEntity<?> deleteOrder(long id) {
        log.info("userBuySeq: {}", id);
        int result = userBuyService.deleteOrder(id);
        if(result == 1) {
            return new ResponseEntity<>("1", HttpStatus.OK);
        }

        return new ResponseEntity<>("주문이 삭제되지 않았습니다.", HttpStatus.NOT_FOUND);
    }

    // 주문 정보 가져오기
    @PostMapping("/shop/order")
    public ResponseEntity<?> getOrderInfo(@RequestParam String orderUid) {
        log.info("주문 정보 가져오기 OrderUid : {}", orderUid);
        UserBuy userBuy = userBuyService.findByOrderUid(orderUid);
        log.info("userbuy : {}", userBuy);
        UserBuyRes userBuyRes = new UserBuyRes(userBuy);
        return new ResponseEntity<>(userBuyRes, HttpStatus.OK);
    }
}
