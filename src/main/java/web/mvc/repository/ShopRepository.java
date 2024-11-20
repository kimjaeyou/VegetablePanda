package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.mvc.domain.Shop;

public interface ShopRepository extends JpaRepository<Shop, Long> {

}
