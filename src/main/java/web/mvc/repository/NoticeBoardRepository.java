package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.NoticeBoard;

public interface NoticeBoardRepository extends JpaRepository<NoticeBoard, Long> {

    @Modifying
    @Query("DELETE FROM NoticeBoard n WHERE n.file IS NULL")
    void deleteAllWithoutFile();
}
