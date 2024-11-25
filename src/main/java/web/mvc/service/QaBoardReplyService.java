package web.mvc.service;

import org.springframework.http.ResponseEntity;
import web.mvc.domain.QaBoardReply;

import java.util.List;

public interface QaBoardReplyService {

    QaBoardReply qaReplySave(QaBoardReply qaBoardReply);

    QaBoardReply qaReplyUpdate(Long boardNoSeq, QaBoardReply qaBoardReply);

    List<QaBoardReply> qaFindAllById(Long boardNoSeq);

    String qaReplyDelete(Long replySeq);


}
