package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.mvc.domain.QaBoardReply;

import java.util.List;

public interface QaBoardReplyRepository extends JpaRepository<QaBoardReply, Long> {
    List<QaBoardReply> findAllByQaBoard_BoardNoSeq(Long boardNoSeq);
}
