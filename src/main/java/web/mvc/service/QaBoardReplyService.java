package web.mvc.service;

import org.springframework.http.ResponseEntity;
import web.mvc.domain.QaBoardReply;
import web.mvc.dto.QAReplyDTO;

import java.util.List;

public interface QaBoardReplyService {

    QaBoardReply qaReplySave(QAReplyDTO qaReplyDTO);

    QaBoardReply qaReplyUpdate(Long boardNoSeq, QAReplyDTO qaReplyDTO);

    List<QaBoardReply> qaFindAllById(Long boardNoSeq);

    String qaReplyDelete(Long replySeq);


}
