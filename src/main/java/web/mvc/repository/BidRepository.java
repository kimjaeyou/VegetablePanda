package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import web.mvc.domain.Bid;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {

    @Query("select b from Bid b where b.managementUser.userSeq = ?1")
    List<Bid> auctionList(Long seq);

    @Query("select b from Bid b where b.auction.auctionSeq = ?1")
    List<Bid> auctionBidList(Long seq);

    @Query("SELECT MAX(b.price) FROM Bid b WHERE b.auction.auctionSeq = :auctionSeq")
    Integer findHighestBidPrice(@Param("auctionSeq") Long auctionSeq);

    @Query("SELECT COUNT(b) FROM Bid b WHERE b.auction.auctionSeq = :auctionSeq")
    Integer countBidsByAuction(@Param("auctionSeq") Long auctionSeq);
}
