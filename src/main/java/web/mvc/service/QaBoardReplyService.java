package web.mvc.service;

import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.QaBoardReply;
import web.mvc.dto.QaBoardReplyDTO;

import java.util.List;

public interface QaBoardReplyService {

    QaBoardReplyDTO saveReply(QaBoardReplyDTO qaBoardReplyDTO);

    QaBoardReplyDTO updateReply(Long replySeq, QaBoardReplyDTO qaBoardReplyDTO);

    List<QaBoardReplyDTO> findRepliesByBoardId(Long boardNoSeq);

    String deleteReply(Long replySeq);
}
