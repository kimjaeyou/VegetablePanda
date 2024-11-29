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
    public QaBoardReplyDTO saveReply(QaBoardReplyDTO qaBoardReplyDTO) {
        QaBoard qaBoard = findQaBoardById(qaBoardReplyDTO.getBoardNoSeq());

        QaBoardReply qaBoardReply = QaBoardReply.builder()
                .comment(qaBoardReplyDTO.getComment())
                .qaBoard(qaBoard)
                .build();

        return QaBoardReplyDTO.fromEntity(qaBoardReplyRepository.save(qaBoardReply));
    }

    /**
     * 댓글 수정
     */
    @Transactional
    @Override
    public QaBoardReplyDTO updateReply(Long replySeq, QaBoardReplyDTO qaBoardReplyDTO) {
        QaBoardReply existingReply = findQaBoardReplyById(replySeq);

        existingReply.setComment(qaBoardReplyDTO.getComment());

        return QaBoardReplyDTO.fromEntity(qaBoardReplyRepository.save(existingReply));
    }

    /**
     * 댓글 조회
     */
    @Transactional(readOnly = true)
    @Override
    public List<QaBoardReplyDTO> findRepliesByBoardId(Long boardNoSeq) {
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
