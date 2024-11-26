package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.Bid;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {

    @Query("select b from Bid b where b.managementUser.userSeq = ?1")
    List<Bid> auctionList(Long seq);


}