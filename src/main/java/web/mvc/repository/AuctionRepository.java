package web.mvc.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import web.mvc.domain.Auction;

import java.time.LocalDateTime;
import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

    @Transactional
    @Query("SELECT a FROM Auction a WHERE a.status = 0 AND a.closeTime <= :currentTime")
    List<Auction> exitAuctions(@Param("currentTime") LocalDateTime currentTime);

    @Modifying
    @Transactional
    @Query("update Auction a set a.status=1 where a.status=0 and a.auctionSeq=?1")
    public void setStatus(Long auctionSeq);

    @Query("select a from Auction a where a.stock.farmerUser.userSeq=?1 and a.status=0")
    public Auction findByUserSeq(Long UserSeq);

    @Query("SELECT a FROM Auction a WHERE a.status = 1 AND a.closeTime > CURRENT_TIMESTAMP ORDER BY a.closeTime ASC")
    List<Auction> findAllActiveAuctions();
}

