package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.mvc.domain.Auction;
import web.mvc.dto.AuctionDTO;
import web.mvc.service.AuctionService;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;
    private final ModelMapper modelMapper;

    // 경매등록
    @PostMapping("/auction")
    public ResponseEntity<?> register(@RequestBody AuctionDTO auctionDTO) {
        log.info("경매 상품 등록 : {}", auctionDTO);

        auctionDTO.setStatus(0); // 경매 승인 대기상태로 등록
        
        Auction auction = modelMapper.map(auctionDTO, Auction.class);
        AuctionDTO result = modelMapper.map(auctionService.insertAuction(auction), AuctionDTO.class);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    // 경매 취소 : 삭제가 아닌 취소 상태로 바꾼다?
    @PatchMapping("/auction/{auctionSeq}")
    public ResponseEntity<?> update(@PathVariable int auctionSeq) {
        log.info("경매 취소~~");
        auctionService.updateAuction(auctionSeq);
        return new ResponseEntity<>("1", HttpStatus.OK);
    }

}
