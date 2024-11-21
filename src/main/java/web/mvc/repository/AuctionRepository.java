package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.mvc.domain.Auction;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
}
