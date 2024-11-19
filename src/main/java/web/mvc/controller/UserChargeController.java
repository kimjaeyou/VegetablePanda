package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import web.mvc.domain.UserCharge;
import web.mvc.dto.UserChargeDTO;
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
    public ResponseEntity<?> charge(@RequestBody UserChargeDTO userChargeDTO) {
        log.info("UserChargeController!!");

        // 주문번호 생성
        String orderUid = userChargeService.generateOrderUid();
        userChargeDTO.setOrderUid(orderUid);
        log.info(orderUid);
        //UserCharge userCharge = userChargeDTO.toUserCharge(userChargeDTO);
        UserCharge usercharge = modelMapper.map(userChargeDTO, UserCharge.class);
        System.out.println(usercharge);

        UserChargeDTO result = modelMapper.map(userChargeService.order(usercharge), UserChargeDTO.class);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
