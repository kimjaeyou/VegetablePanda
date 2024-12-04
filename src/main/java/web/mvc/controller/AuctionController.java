package web.mvc.controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.protocol.HTTP;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.mvc.domain.Auction;
import web.mvc.dto.*;
import web.mvc.service.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AuctionController {

    private final ServletContext servletContext;
    private final AuctionService auctionService;
    private final ModelMapper modelMapper;
    private final BidService bidService;
    private final UserBuyService userBuyService;
    private final LikeService likeService;
    private final NotificationService notificationService;


    // 경매등록
    @PostMapping("/auction")
    public ResponseEntity<?> register(@RequestBody AuctionDTO auctionDTO, @RequestParam int price) {
        log.info("경매 상품 등록 : {}", auctionDTO);

        AuctionDTO result = modelMapper.map(auctionService.insertAuction(auctionDTO,price), AuctionDTO.class);
        /*
            redis 등록
         */
        if(result!=null){
            likeService.getLikeUserSeq(auctionDTO.getStockSeq());
        }
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    // 경매 취소 : 삭제가 아닌 취소 상태로 바꾼다?
    // 경매 종료 : highestBidDTO를 레디스에서 꺼내서
    @PatchMapping("/auction/{auctionSeq}")
    public ResponseEntity<?> update(@PathVariable Long auctionSeq) {
        log.info("경매 종료~~");
        int n=auctionService.updateAuction(auctionSeq);
        if(n==1){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!");
            notificationService.sendMessageToTopic("/top/notifications","1");
        }
        return new ResponseEntity<>("1", HttpStatus.OK);

    }

    @PostMapping("/tests")
    public void test(){
        List<String> garakData = (List<String>) servletContext.getAttribute("garakData");
        System.out.println("start:"+garakData);
    }

    @GetMapping("/auction/{userSeq}")
    public ResponseEntity<?> getAuction(@PathVariable Long userSeq) {
        Auction result =auctionService.getAuction(userSeq);
        if(result!=null) {
            AuctionDTO auctionDTO = AuctionDTO.builder()
                    .auctionSeq(result.getAuctionSeq())
                    .count(result.getCount()).stockSeq(result.getStock().getStockSeq())
                    .closeTime(String.valueOf(result.getCloseTime()))
                    .status(result.getStatus())
                    .startTime(String.valueOf(result.getStartTime()))
                    .build();
            HighestBidDTO highestBidDTO = bidService.checkHighestBid(result.getAuctionSeq(), userSeq);
        }
        return new ResponseEntity<>(result, HttpStatus.CREATED);

    }


    @GetMapping("/highestBid/{userSeq}")
    public ResponseEntity<?> getHighestBid(@PathVariable Long userSeq) {
        Auction result = auctionService.getAuction(userSeq);
        System.out.println(result.getAuctionSeq());
        HighestBidDTO highestBidDTO = null;
        if(result!=null) {
                highestBidDTO = bidService.getHighestBid(result.getAuctionSeq());
        }
        return new ResponseEntity<>(highestBidDTO, HttpStatus.CREATED);
    }



    @GetMapping("/current")
    public ResponseEntity<List<AuctionStatusDTO>> getCurrentAuctions() {
        return ResponseEntity.ok(auctionService.getCurrentAuctions());
    }

    @GetMapping("/price/{productName}")
    public ResponseEntity<List<GarakTotalCost>> testApi(HttpServletRequest req,@PathVariable String productName) {
        ServletContext app = req.getServletContext();
        List<GarakTotalCost> dto=(List<GarakTotalCost>)app.getAttribute("garakData");
        List<GarakTotalCost> saveCost = new ArrayList<>();
        for(GarakTotalCost garakTotalCost:dto){
            if(garakTotalCost.getGarak_name().equals(productName)) {
                saveCost.add(garakTotalCost);
            }
        }
        return new ResponseEntity<>(saveCost, HttpStatus.CREATED);
    }

    @GetMapping("/buy/{stockSeq}")
    public ResponseEntity<List<UserBuyListByStockDTO>> buyList(HttpServletRequest req, @PathVariable Long stockSeq) {
        List<UserBuyListByStockDTO> userBuyListByStockDTOList = userBuyService.geUserBuyListByStockDtos(stockSeq);
        return new ResponseEntity<>(userBuyListByStockDTOList, HttpStatus.CREATED);
    }



}
