package web.mvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.QaBoardReply;
import web.mvc.exception.DMLException;
import web.mvc.exception.ErrorCode;
import web.mvc.repository.QaBoardReplyRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class QaBoardReplyServiceImpl implements QaBoardReplyService {

    private final QaBoardReplyRepository qaBoardReplyRepository;
    /**
     * 질문 등록
     * */
    @Override
    @Transactional
    public QaBoardReply qaReplySave(QaBoardReply qaBoardReply) {


        return qaBoardReplyRepository.save(qaBoardReply);
    }


    /**
     * 질문 수정
     * */
    @Override
    @Transactional
    public QaBoardReply qaReplyUpdate(Long boardNoSeq, QaBoardReply qaBoardReply) {
        QaBoardReply existingReply = qaBoardReplyRepository.findById(boardNoSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.UPDATE_FAILED));

        existingReply.setComment(qaBoardReply.getComment());

        return qaBoardReplyRepository.save(existingReply);
    }


    /**
     * 질문 조회
     * */
    @Override
    @Transactional
    public QaBoardReply qaReplyFindBySeq(Long boardNoSeq) {


        return qaBoardReplyRepository.findById(boardNoSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));
    }



    /**
     * 질문 삭제
     * */
    @Override
    @Transactional
    public String qaReplyDelete(Long boardNoSeq) {

        QaBoardReply existingReply = qaBoardReplyRepository.findById(boardNoSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));

        qaBoardReplyRepository.delete(existingReply);

        return "삭제 완료";
    }
}
