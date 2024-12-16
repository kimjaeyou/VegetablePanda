package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.mvc.dto.UserBuyDetailDTO;
import web.mvc.dto.UserBuyDetailInfoDTO;
import web.mvc.service.UserBuyDetailService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/userBuyDetail")
public class UserBuyDetailController {
    // 주문 상세 품목 정보 가져오기
    private final UserBuyDetailService userBuyDetailService;

    @PostMapping("/orderInfo")
    public ResponseEntity<?> findUserBuyDetailInfoes (@RequestBody List<UserBuyDetailDTO> userBuyDetails) {
        List<Long> stockSeqs = userBuyDetails.stream()
                .map(UserBuyDetailDTO::getStockSeq)
                .collect(Collectors.toList());
        log.info("findUserBuyDetailInfoes 호출, 상품 정보 가져오기");
        log.info("stockSeq : {}", stockSeqs);

        Long userBuySeq = userBuyDetails.get(0).getBuySeq();
        log.info("userBuySeq : {}", userBuySeq);

        List<UserBuyDetailInfoDTO> detailInfoes = userBuyDetailService.getUserBuyDetailInfoes(stockSeqs, userBuySeq);
        log.info(detailInfoes.toString());
//        for(UserBuyDetailDTO detailDTO : details) {
//            // DTO의 stockSeq 값으로 상품정보 가져와서 UserBuyDetailInfoDTO에 담아주기
//            detailInfoes.add(userBuyDetailService.getUserBuyDetailInfo(detailDTO.getStockSeq()));
//        }
        return new ResponseEntity<>(detailInfoes, HttpStatus.OK);
    }
}
