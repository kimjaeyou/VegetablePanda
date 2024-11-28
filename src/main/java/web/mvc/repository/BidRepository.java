package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.Bid;
import web.mvc.dto.BidAuctionDTO;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {

    @Query("SELECT new web.mvc.dto.BidAuctionDTO(b.bidSeq, s.content, a.count, b.price, b.insertDate, s.farmerUser.name, a.status) " +
            "FROM Bid b " +
            "JOIN Auction a ON b.auction.auctionSeq = a.auctionSeq " +
            "JOIN Stock s ON s.stockSeq = a.stock.stockSeq " +
            "WHERE b.managementUser.userSeq = ?1")
    List<BidAuctionDTO> auctionList(Long seq);


}