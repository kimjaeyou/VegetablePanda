package web.mvc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.Auction;
import web.mvc.exception.AuctionException;
import web.mvc.exception.ErrorCode;
import web.mvc.repository.AuctionRepository;

@Service
@Slf4j
@Transactional
public class AuctionServiceImpl implements AuctionService {

    @Autowired
    private AuctionRepository auctionRepository;

    @Override
    public Auction insertAuction(Auction auction) {
        log.info("insert auction ServiceImpl");

        return auctionRepository.save(auction);
    }
    //경매종료
    @Override
    public int updateAuction(long auctionSeq) {

        Auction dbAuction = auctionRepository.findById(auctionSeq).orElseThrow(()->new AuctionException(ErrorCode.AUCTION_NOTFOUND));
        dbAuction.setStatus(1); // 등록 취소? : 추가적인 논의 필요
        //highestBid 꺼내고


        return 1;
    }





}
