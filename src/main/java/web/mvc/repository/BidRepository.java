package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import web.mvc.domain.Bid;
import web.mvc.dto.BidAuctionDTO;
import web.mvc.dto.BidListDTO;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {

    @Query("SELECT new web.mvc.dto.BidAuctionDTO(b.bidSeq, s.content, a.count, b.price, b.insertDate, s.farmerUser.name, a.status) " +
            "FROM Bid b " +
            "JOIN Auction a ON b.auction.auctionSeq = a.auctionSeq " +
            "JOIN Stock s ON s.stockSeq = a.stock.stockSeq " +
            "WHERE b.managementUser.userSeq = ?1")
    List<BidAuctionDTO> auctionList(Long seq);


    @Query("select new web.mvc.dto.BidListDTO(b.price,b.insertDate,c.comName) from Bid b left join CompanyUser c on b.auction.auctionSeq = ?1 order by b.insertDate ")
    List<BidListDTO> auctionCompanyBidList(Long seq);

    @Query("select new web.mvc.dto.BidListDTO(b.price,b.insertDate, u.name) from Bid b left join User u on b.auction.auctionSeq = ?1 order by b.insertDate")
    List<BidListDTO> auctionUerBidList(Long seq);


    @Query("SELECT MAX(b.price) FROM Bid b WHERE b.auction.auctionSeq = :auctionSeq")
    Integer findHighestBidPrice(@Param("auctionSeq") Long auctionSeq);

    @Query("SELECT COUNT(b) FROM Bid b WHERE b.auction.auctionSeq = :auctionSeq")
    Integer countBidsByAuction(@Param("auctionSeq") Long auctionSeq);
}
