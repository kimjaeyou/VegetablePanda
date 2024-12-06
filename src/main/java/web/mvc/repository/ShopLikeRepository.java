package web.mvc.repository;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.ShopLike;
import web.mvc.dto.ShopLikeResponseDTO;

import java.util.List;

public interface ShopLikeRepository extends JpaRepository<ShopLike, Long> {

        @Transactional
        @Modifying
        @Query(value = "INSERT INTO shop_like (shop_seq, user_seq) VALUES (:shopSeq, :userSeq)", nativeQuery = true)
        void insertShopLike(Long userSeq, Long shopSeq);


        @Query(value = "select * from shop_like where shop_seq =:shopSeq and user_seq =:userSeq", nativeQuery = true)
        ShopLike findByUserSeqAndShopSeq(Long userSeq, Long shopSeq);

        @Query("SELECT new web.mvc.dto.ShopLikeResponseDTO(" +
                "s.shopSeq, " +
                "s.stock.stockSeq, " +
                "s.stock.product.productName, " +
                "s.stock.product.productCategory.content, " +
                "s.price, " +
                "s.stock.file.path, " +
                "s.stock.content, " +
                "s.stock.count, " +
                "s.stock.stockGrade.grade, " +
                "s.stock.stockOrganic.organicStatus, " +
                "s.stock.file.path) " +
                "FROM Shop s " +
                "JOIN ShopLike sl ON s.shopSeq = sl.shop.shopSeq " +
                "WHERE sl.managementUser.userSeq = :userSeq AND sl.state = true")
        List<ShopLikeResponseDTO> findLikedShopsByUserSeq(@Param("userSeq") Long userSeq);

}
