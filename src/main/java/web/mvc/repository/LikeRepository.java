package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import web.mvc.domain.Likes;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes, Long> {
    @Query("SELECT l FROM Likes l WHERE l.managementUser.userSeq = :userSeq AND l.farmerUser.userSeq = :farmerSeq")
    Likes findByUserSeqAndFarmerSeq(@Param("userSeq") Long userSeq, @Param("farmerSeq") Long farmerSeq);
}

