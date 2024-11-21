package web.mvc.service;

import web.mvc.domain.Bid;
import web.mvc.dto.BidDTO;

public interface BidService {

    //새로운 입찰자가 입찰 시도
    public Bid bid(Bid newBidder);
}
