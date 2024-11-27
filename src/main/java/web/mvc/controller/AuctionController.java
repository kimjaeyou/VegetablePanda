package web.mvc.controller;

import jakarta.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.mvc.domain.Auction;
import web.mvc.dto.AuctionDTO;
import web.mvc.dto.HighestBidDTO;
import web.mvc.service.AuctionService;
import web.mvc.service.BidService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AuctionController {

    private final ServletContext servletContext;
    private final AuctionService auctionService;
    private final ModelMapper modelMapper;
    private final BidService bidService;
    // 경매등록
    @PostMapping("/auction")
    public ResponseEntity<?> register(@RequestBody AuctionDTO auctionDTO, @RequestParam int price) {
        log.info("경매 상품 등록 : {}", auctionDTO);

        AuctionDTO result = modelMapper.map(auctionService.insertAuction(auctionDTO,price), AuctionDTO.class);
        /*
            redis 등록
         */

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    // 경매 취소 : 삭제가 아닌 취소 상태로 바꾼다?
    // 경매 종료 : highestBidDTO를 레디스에서 꺼내서
    @PostMapping("/auction/{auctionSeq}")
    public ResponseEntity<?> update(@PathVariable Long auctionSeq) {
        log.info("경매 종료~~");
        auctionService.updateAuction(auctionSeq);
        return new ResponseEntity<>("1", HttpStatus.OK);

    }

    @PostMapping("/tests")
    public void test(){
        List<String> garakData = (List<String>) servletContext.getAttribute("garakData");
        System.out.println("start:"+garakData);
    }

    @GetMapping("/auction/{userSeq}")
    public ResponseEntity<?> getAuction(@PathVariable Long userSeq) {
        AuctionDTO result = modelMapper.map(auctionService.getAuction(userSeq), AuctionDTO.class);
        HighestBidDTO highestBidDTO = bidService.checkHighestBid(result.getAuctionSeq(),userSeq);
        return new ResponseEntity<>(result, HttpStatus.CREATED);

    }


    @GetMapping("/highestBid/{userSeq}")
    public ResponseEntity<?> getHighestBid(@PathVariable Long userSeq) {
        Auction result = auctionService.getAuction(userSeq);
        System.out.println(result.getAuctionSeq());
        HighestBidDTO highestBidDTO = bidService.getHighestBid(result.getAuctionSeq());
        return new ResponseEntity<>(highestBidDTO, HttpStatus.CREATED);

    }


}
