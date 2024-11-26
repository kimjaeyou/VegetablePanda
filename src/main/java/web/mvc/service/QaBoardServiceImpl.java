package web.mvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.NoticeBoard;
import web.mvc.domain.QaBoard;
import web.mvc.dto.QaDTO;
import web.mvc.exception.DMLException;
import web.mvc.exception.ErrorCode;
import web.mvc.repository.QaBoardRepository;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class QaBoardServiceImpl implements QaBoardService {

    private final QaBoardRepository qaBoardRepository;
    QaDTO qaDTO = new QaDTO();

    /**
     * 질문 등록
     * */
    @Override
    public QaDTO qaSave(QaBoard qaBoard) {


        String writerId = getCurrentUserId();

        qaBoard.setReadnum(0);
        qaBoard.setFile(null);

        qaDTO.setWriterId(writerId);
        QaBoard savedQaBoard = qaBoardRepository.save(qaBoard);
        return toDto(savedQaBoard, writerId);
    }


    /**
     * 질문 수정
     * */
    @Override
    public QaDTO qaUpdate(Long boardNoSeq, QaBoard qaBoard) {

        QaBoard qa = qaBoardRepository.findById(boardNoSeq)
                .orElseThrow(()->new DMLException(ErrorCode.NOTFOUND_BOARD));

        qa.setSubject(qaBoard.getSubject());
        qa.setContent(qaBoard.getContent());

        return toDto(qaBoardRepository.save(qa), getCurrentUserId());
    }


    /**
     * 질문 조회
     * */
    @Override
    @Transactional(readOnly = true)
    public QaDTO qaFindBySeq(Long boardNoSeq) {

        QaBoard qaBoard = qaBoardRepository.findById(boardNoSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));
        return toDto(qaBoard, getCurrentUserId());
    }


    /**
     * 전체 조회
     * */
    @Override
    public List<QaDTO> qaFindAll() {
        String writerId = getCurrentUserId();

        return qaBoardRepository.findAll().stream()
                .map(qaBoard -> toDto(qaBoard, writerId))
                .toList();
    }


    /**
     * 질문 삭제
     * */
    @Override
    public String qaDelete(Long boardNoSeq) {
        QaBoard qaBoard = qaBoardRepository.findById(boardNoSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));

        qaBoardRepository.delete(qaBoard);

        return "정상적으로 삭제되었습니다.";
    }

    @Override
    public QaBoard increaseReadnum(Long boardNoSeq) {
        QaBoard qaBoard = qaBoardRepository.findById(boardNoSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));

        qaBoard.setReadnum(qaBoard.getReadnum() + 1);
        return qaBoardRepository.save(qaBoard);
    }

    @Override
    public QaDTO toDto(QaBoard qaBoard, String writerId) {
        return QaDTO.builder()
                .boardNoSeq(qaBoard.getBoardNoSeq())
                .subject(qaBoard.getSubject())
                .content(qaBoard.getContent())
                .readnum(qaBoard.getReadnum())
                .regDate(qaBoard.getRegDate())
                .writerId(writerId) // 작성자 추가
                .build();
    }

    /**
     * 현재 로그인한 사용자 ID 가져오기
     */
    private String getCurrentUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
