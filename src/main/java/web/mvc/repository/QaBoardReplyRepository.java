package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.mvc.domain.NoticeBoard;
import web.mvc.domain.QaBoardReply;

public interface QaBoardReplyRepository extends JpaRepository<QaBoardReply, Long> {

}
