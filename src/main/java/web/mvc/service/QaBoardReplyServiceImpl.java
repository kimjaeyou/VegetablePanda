package web.mvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.QaBoard;
import web.mvc.domain.QaBoardReply;
import web.mvc.dto.QAReplyDTO;
import web.mvc.exception.DMLException;
import web.mvc.exception.ErrorCode;
import web.mvc.repository.QaBoardReplyRepository;
import web.mvc.repository.QaBoardRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class QaBoardReplyServiceImpl implements QaBoardReplyService {

    private final QaBoardReplyRepository qaBoardReplyRepository;
    private final QaBoardRepository qaBoardRepository;

    /**
     * 댓글 등록
     */
    @Override
    @Transactional
    public QaBoardReply qaReplySave(QAReplyDTO qaReplyDTO) {
        QaBoard qaBoard = qaBoardRepository.findById(qaReplyDTO.getBoardNoSeq())
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));

        QaBoardReply qaBoardReply = QaBoardReply.builder()
                .comment(qaReplyDTO.getComment())
                .qaBoard(qaBoard)
                .build();

        return qaBoardReplyRepository.save(qaBoardReply);
    }

    /**
     * 댓글 수정
     */
    @Override
    @Transactional
    public QaBoardReply qaReplyUpdate(Long boardNoSeq, QAReplyDTO qaReplyDTO) {
        QaBoardReply existingReply = qaBoardReplyRepository.findById(qaReplyDTO.getBoardNoSeq())
                .orElseThrow(() -> new DMLException(ErrorCode.UPDATE_FAILED));

        if (!existingReply.getQaBoard().getBoardNoSeq().equals(boardNoSeq)) {
            throw new DMLException(ErrorCode.NOTFOUND_REPLY);
        }

        existingReply.setComment(qaReplyDTO.getComment());
        return qaBoardReplyRepository.save(existingReply);
    }

    /**
     * 댓글 조회
     */
    @Override
    @Transactional(readOnly = true)
    public List<QaBoardReply> qaFindAllById(Long boardNoSeq) {
        List<QaBoardReply> replies = qaBoardReplyRepository.findAllByQaBoard_BoardNoSeq(boardNoSeq);
        if (replies.isEmpty()) {
            throw new DMLException(ErrorCode.NOTFOUND_BOARD);
        }
        return replies;
    }

    /**
     * 댓글 삭제
     */
    @Override
    @Transactional
    public String qaReplyDelete(Long replySeq) {
        QaBoardReply existingReply = qaBoardReplyRepository.findById(replySeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));

        qaBoardReplyRepository.delete(existingReply);
        return "삭제 완료";
    }
    }

