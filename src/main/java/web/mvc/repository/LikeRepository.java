package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.mvc.domain.Likes;

public interface LikeRepository extends JpaRepository<Likes, Long> {

}
