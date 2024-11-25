package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import web.mvc.domain.Shop;
import web.mvc.dto.ShopListDTO;

import java.util.List;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    @Query("SELECT new web.mvc.dto.ShopListDTO(s.shopSeq, st.stockSeq, st.content, s.price, st.count, " +
            "s.insertDate, p.productName, sg.grade, so.oranicStatus) " +
            "FROM Shop s " +
            "JOIN s.stock st " +
            "JOIN st.product p " +
            "JOIN st.stockGrade sg " +
            "JOIN st.stockOrganic so " +
            "WHERE st.status = 1 " +
            "ORDER BY s.insertDate DESC")
    List<ShopListDTO> findAllShopItems();

    @Query("SELECT s.shopSeq, st.stockSeq, st.status, st.content " +
            "FROM Shop s " +
            "JOIN s.stock st " +
            "WHERE st.status = 1")
    List<Object[]> findDebugInfo();
}