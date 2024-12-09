package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import web.mvc.domain.FarmerUser;
import web.mvc.domain.Likes;
import web.mvc.dto.LikeDTO;
import web.mvc.dto.UserLikeDTO;

import java.util.List;
import java.util.List;
import java.util.Optional;


public interface LikeRepository extends JpaRepository<Likes, Long> {
    //@Query("SELECT new web.mvc.dto.LikeDTO(l.likeSeq )from Likes l")
    //List<LikeDTO> likeList(Long seq);

    @Modifying
    @Query("DELETE from Likes l where l.likeSeq = ?1 ")
    int likeDelete(Long seq, Long likeSeq);

    @Query("SELECT l FROM Likes l WHERE l.managementUser.userSeq = :userSeq AND l.farmerUser.userSeq = :farmerSeq")
    Likes findByUserSeqAndFarmerSeq(@Param("userSeq") Long userSeq, @Param("farmerSeq") Long farmerSeq);

    @Query("SELECT l.managementUser.userSeq FROM Likes l WHERE l.farmerUser.userSeq = :farmerSeq and l.state=true")
    List<Long> findUserSeq(@Param("farmerSeq") Long farmerSeq);

    @Query("SELECT f FROM Likes l JOIN l.farmerUser f WHERE l.farmerUser.userSeq = :farmerSeq")
    FarmerUser findFarmerUserByFarmerSeq(@Param("farmerSeq") Long farmerSeq);

    @Query("SELECT l FROM Likes l JOIN FETCH l.farmerUser f WHERE l.farmerUser.userSeq = :farmerSeq")
    List<Likes> findLikesWithFarmerUserByFarmerSeq(@Param("farmerSeq") Long farmerSeq);

    @Query("select l.state from Likes l where l.farmerUser.userSeq = ?1 and l.managementUser.userSeq = ?2")
    Boolean likeState(Long farmerSeq, Long userSeq);

    @Query("select new web.mvc.dto.LikeDTO(l.managementUser.userSeq, l.farmerUser.userSeq, m.file.path, l.farmerUser.name) from Likes l " +
            "join ManagementUser m on l.farmerUser.userSeq = m.userSeq where l.managementUser.userSeq = ?1 and l.state = true")
    List<LikeDTO> findAllLikesByUserSeq(Long userSeq);
}

