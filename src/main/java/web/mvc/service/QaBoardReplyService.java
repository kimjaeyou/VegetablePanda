package web.mvc.service;

import web.mvc.domain.QaBoardReply;

public interface QaBoardReplyService {

    QaBoardReply qaReplySave(QaBoardReply qaBoardReply);

    QaBoardReply qaReplyUpdate(Long boardNoSeq, QaBoardReply qaBoardReply);

    QaBoardReply qaReplyFindBySeq(Long boardNoSeq);

    QaBoardReply qaReplyDelete(Long boardNoSeq);


}
