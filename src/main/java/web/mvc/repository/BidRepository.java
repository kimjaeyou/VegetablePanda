package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.Bid;
import web.mvc.dto.AuctionDTO2;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {

    @Query("select b.bidSeq , b.price, b.insertDate , a.count , a.status, s.content , s.farmerUser.name from Bid b join Auction a on b.auction.auctionSeq = a.auctionSeq join Stock s on a.stock.stockSeq = s.stockSeq where b.managementUser.userSeq = ?1")

    List<AuctionDTO2> auctionList(Long seq);


}
