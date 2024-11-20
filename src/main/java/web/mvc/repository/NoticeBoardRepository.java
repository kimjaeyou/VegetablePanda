package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.mvc.domain.NoticeBoard;

public interface NoticeBoardRepository extends JpaRepository<NoticeBoard, Integer> {

}
