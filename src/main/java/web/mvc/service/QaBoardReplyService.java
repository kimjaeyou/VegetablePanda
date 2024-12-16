package web.mvc.service;

import org.springframework.transaction.annotation.Transactional;
import web.mvc.dto.QaBoardReplyDTO;

import java.util.List;

public interface QaBoardReplyService {


    QaBoardReplyDTO saveReply(Long boardNoSeq, QaBoardReplyDTO qaBoardReplyDTO);

    QaBoardReplyDTO updateReply(Long boardNoSeq, Long replySeq, QaBoardReplyDTO qaBoardReplyDTO);

    List<QaBoardReplyDTO> findRepliesByBoardId(Long boardNoSeq);

    String deleteReply(Long replySeq);
}
