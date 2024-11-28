
package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import web.mvc.domain.Shop;
import web.mvc.dto.ShopListDTO;

import java.util.List;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    @Query("SELECT new web.mvc.dto.ShopListDTO(s.shopSeq, s.stock.stockSeq, s.stock.content, " +
            "s.price, s.stock.count, " +
            "CAST(s.insertDate AS string), " +
            "s.stock.product.productName, " +
            "s.stock.stockGrade.grade, " +
            "s.stock.stockOrganic.organicStatus, " +
            "s.stock.file.path)" +
            "FROM Shop s " +
            "WHERE s.stock.status = 1 " +
            "ORDER BY s.insertDate DESC")
    List<ShopListDTO> findAllShopItems();
}