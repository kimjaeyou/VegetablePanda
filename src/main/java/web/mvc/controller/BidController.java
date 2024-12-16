package web.mvc.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.mvc.dto.*;
import web.mvc.service.BidService;
import web.mvc.service.SendTopService;

import java.util.List;


@RestController
@Slf4j
@RequiredArgsConstructor
public class BidController {


    private final ModelMapper modelMapper;

    private final BidService bidService;
    private final SendTopService sendTopService;


    // 입찰
    @PostMapping("/bid")
    public ResponseEntity<?> userBid(@RequestBody BidDTO newBidder) {
        log.info("입찰 등록 : {}", newBidder.getUserSeq());
        HighestBidDTO highestBidDTO = bidService.checkHighestBid(newBidder.getAuctionSeq(),newBidder.getUserSeq());
        System.out.println(highestBidDTO.getPrice()+"가격 "+ highestBidDTO.getUserSeq()+"옥션번호"+highestBidDTO.getAuctionSeq());
        UserTempWalletDTO userTempWalletDTO = bidService.checkUserTempWallet(newBidder.getUserSeq());
        BidDTO result = modelMapper.map(bidService.bid(newBidder,highestBidDTO,userTempWalletDTO), BidDTO.class);
        if(result!=null){
            String message = "1";
            sendTopService.sendTopMessage(message);
        }
        /*
            redis 등록
         */

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/bidCom/{auctionSeq}")
    public ResponseEntity<?> bidList1(@PathVariable Long auctionSeq) {
        List<BidListDTO> result = bidService.getComBids(auctionSeq);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/bidUser/{auctionSeq}")
    public ResponseEntity<?> bidList2(@PathVariable Long auctionSeq) {
        List<BidListDTO> result = bidService.getUserBids(auctionSeq);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("userTempWallet/{userSeq}")
    public ResponseEntity<?> getUserWallet(@PathVariable Long userSeq) {
        UserTempWalletDTO result = bidService.checkUserTempWallet(userSeq);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }





}
