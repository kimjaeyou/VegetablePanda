package web.mvc.repository;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.ShopLike;

public interface ShopLikeRepository extends JpaRepository<ShopLike, Integer> {

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO shop_like (shop_seq, user_seq) VALUES (:shopSeq, :userSeq)", nativeQuery = true)
    void insertShopLike(@Param("shopSeq") Long shopSeq, @Param("userSeq") Long userSeq);
}
