package web.mvc.service;

import web.mvc.domain.Bid;
import web.mvc.dto.*;

import java.util.List;

public interface BidService {

    /*
          1.redis에 유저 지갑 임시지갑 존재하는지? ->없으면 userWallet 정보를 userTempWallet에 담아서 redis에 저장
          2.redis에서 highestBid정보 갖고오기(경매 등록과 동시에 캐쉬에 무조건 등록할 것- 없는경우 생각 x)
          3.임시지갑의 포인트가 입찰금 보다 큰지?
          4.기존 입찰자 존재시(userSeq 0이 아닐시) -> 알림(나중에 구현)
          5. 포인트차감 (role 기준으로 일반인은 100퍼, 업체는 10퍼)
          5.redis watch로 데이터 수정이 이미 된 상태면 rollback
       */
    Bid bid(BidDTO newBidder, HighestBidDTO highestBid, UserTempWalletDTO userTempWallet);

    HighestBidDTO checkHighestBid(Long auctionSeq, Long userSeq);

    UserTempWalletDTO checkUserTempWallet(Long userSeq);

    HighestBidDTO getHighestBid(Long auctionSeq);

    List<BidListDTO> getComBids(Long auctionSeq);

    List<BidListDTO> getUserBids(Long auctionSeq);

}
