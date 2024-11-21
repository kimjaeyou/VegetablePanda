package web.mvc.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import web.mvc.domain.Auction;
import web.mvc.domain.Bid;
import web.mvc.dto.AuctionDTO;
import web.mvc.dto.BidDTO;
import web.mvc.service.BidService;

@RestController
@Slf4j
@RequiredArgsConstructor
public class BidController {


    private final ModelMapper modelMapper;

    private final BidService bidService;


    // 입찰
    @PostMapping("/bid")
    public ResponseEntity<?> register(@RequestBody BidDTO bidDTO) {
        log.info("입찰 등록 : {}", bidDTO);


        Bid newBidder = modelMapper.map(bidDTO, Bid.class);
        BidDTO result = modelMapper.map(bidService.bid(newBidder), BidDTO.class);
        /*
            redis 등록
         */

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }



}
