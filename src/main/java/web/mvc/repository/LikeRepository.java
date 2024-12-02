package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.Likes;
import web.mvc.dto.LikeDTO;

import java.util.List;

public interface LikeRepository extends JpaRepository<Likes, Long> {

//@Query("SELECT new web.mvc.dto.LikeDTO(l.likeSeq )from Likes l")
//List<LikeDTO> likeList(Long seq);

@Modifying
@Query("DELETE from Likes l where l.likeSeq = ?1 ")
int likeDelete(Long seq, Long likeSeq);

}
