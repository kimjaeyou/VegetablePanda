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


    @Query("SELECT new web.mvc.dto.BidListDTO(b.bidSeq, b.price, b.insertDate, c.comName)\n" +
            "FROM Bid b\n" +
            "JOIN CompanyUser c ON b.managementUser.userSeq = c.userSeq\n" +
            "WHERE b.auction.auctionSeq = :auctionSeq\n" +
            "GROUP BY b.bidSeq, b.price, b.insertDate, c.comName\n" +
            "ORDER BY b.bidSeq")
    List<BidListDTO> auctionCompanyBidList(@Param("auctionSeq") Long auctionSeq);

    @Query("SELECT new web.mvc.dto.BidListDTO(b.bidSeq, b.price, b.insertDate, u.name)\n" +
            "FROM Bid b\n" +
            "JOIN User u ON b.managementUser.userSeq =u.userSeq\n" +
            "WHERE b.auction.auctionSeq = :auctionSeq\n" +
            "GROUP BY b.bidSeq, b.price, b.insertDate, u.name\n" +
            "ORDER BY b.bidSeq")    List<BidListDTO> auctionUerBidList(@Param("auctionSeq") Long auctionSeq);


    @Query("SELECT MAX(b.price) FROM Bid b WHERE b.auction.auctionSeq = :auctionSeq")
    Integer findHighestBidPrice(@Param("auctionSeq") Long auctionSeq);

    @Query("SELECT COUNT(b) FROM Bid b WHERE b.auction.auctionSeq = :auctionSeq")
    Integer countBidsByAuction(@Param("auctionSeq") Long auctionSeq);
}
