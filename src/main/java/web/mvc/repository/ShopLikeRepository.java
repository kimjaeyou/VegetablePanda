package web.mvc.repository;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.ShopLike;

public interface ShopLikeRepository extends JpaRepository<ShopLike, Long> {

        @Transactional
        @Modifying
        @Query(value = "INSERT INTO shop_like (shop_seq, user_seq) VALUES (:shopSeq, :userSeq)", nativeQuery = true)
        void insertShopLike(Long userSeq, Long shopSeq);


        @Query(value = "select * from shop_like where shop_seq =:shopSeq and user_seq =:userSeq", nativeQuery = true)
        ShopLike findByUserSeqAndShopSeq(Long userSeq, Long shopSeq);

}
