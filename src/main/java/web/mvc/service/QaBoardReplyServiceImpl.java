package web.mvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.QaBoard;
import web.mvc.domain.QaBoardReply;
import web.mvc.dto.QaBoardReplyDTO;
import web.mvc.exception.DMLException;
import web.mvc.exception.ErrorCode;
import web.mvc.repository.QaBoardReplyRepository;
import web.mvc.repository.QaBoardRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class QaBoardReplyServiceImpl implements QaBoardReplyService {

    private final QaBoardReplyRepository qaBoardReplyRepository;
    private final QaBoardRepository qaBoardRepository;

    /**
     * 댓글 등록
     */
    @Transactional
    @Override
    public QaBoardReplyDTO saveReply(Long boardNoSeq, QaBoardReplyDTO qaBoardReplyDTO) {
        log.info("댓글 등록 요청: boardNoSeq={}, comment={}", boardNoSeq, qaBoardReplyDTO.getComment());

        QaBoard qaBoard = findQaBoardById(boardNoSeq);
        QaBoardReply qaBoardReply = qaBoardReplyDTO.toEntity();
        qaBoardReply.setQaBoard(qaBoard);
        QaBoardReply savedReply = qaBoardReplyRepository.save(qaBoardReply);
        return QaBoardReplyDTO.fromEntity(savedReply);
    }

    /**
     * 댓글 수정
     */
    @Transactional
    @Override
    public QaBoardReplyDTO updateReply(Long boardNoSeq, Long replySeq, QaBoardReplyDTO qaBoardReplyDTO) {
        log.info("댓글 수정 요청: boardNoSeq={}, replySeq={}, comment={}", boardNoSeq, replySeq, qaBoardReplyDTO.getComment());

        QaBoardReply existingReply = findQaBoardReplyById(replySeq);

        existingReply.setComment(qaBoardReplyDTO.getComment());

        QaBoardReply updatedReply = qaBoardReplyRepository.save(existingReply);
        return QaBoardReplyDTO.fromEntity(updatedReply);
    }

    /**
     * 댓글 조회
     */
    @Transactional(readOnly = true)
    @Override
    public List<QaBoardReplyDTO> findRepliesByBoardId(Long boardNoSeq) {
        log.info("댓글 조회 요청: boardNoSeq={}", boardNoSeq);

        return qaBoardReplyRepository.findAllByQaBoard_BoardNoSeq(boardNoSeq)
                .stream()
                .map(QaBoardReplyDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 댓글 삭제
     */
    @Transactional
    @Override
    public String deleteReply(Long replySeq) {
        log.info("댓글 삭제 요청: replySeq={}", replySeq);

        QaBoardReply existingReply = findQaBoardReplyById(replySeq);
        qaBoardReplyRepository.delete(existingReply);
        return "댓글이 성공적으로 삭제되었습니다.";
    }

    /**
     * 게시판 찾기
     */
    private QaBoard findQaBoardById(Long boardNoSeq) {
        return qaBoardRepository.findById(boardNoSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));
    }

    /**
     * 댓글 찾기
     */
    private QaBoardReply findQaBoardReplyById(Long replySeq) {
        return qaBoardReplyRepository.findById(replySeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_REPLY));
    }
}
