package web.mvc.service;

import web.mvc.domain.Auction;

public interface AuctionService {
    /**
     * 경매 등록 : 재고번호, 판매 수량, 경매 시작가, (시작시간, 종료시간) 입력 후 등록
     */
    public Auction insertAuction(Auction auction);

    /**
     * 경매 취소 : 경매번호를 받아 경매 취소 신청
     */
    public int updateAuction(long auctionSeq);
}
