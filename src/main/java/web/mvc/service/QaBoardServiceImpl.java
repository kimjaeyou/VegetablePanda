package web.mvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.NoticeBoard;
import web.mvc.domain.QaBoard;
import web.mvc.exception.DMLException;
import web.mvc.exception.ErrorCode;
import web.mvc.repository.QaBoardRepository;
import java.time.LocalDateTime;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class QaBoardServiceImpl implements QaBoardService {

    private final QaBoardRepository qaBoardRepository;

    /**
     * 질문 등록
     * */
    @Override
    public QaBoard qaSave(QaBoard qaBoard) {


        if (qaBoard.getRegDate() == null) {
            qaBoard.setRegDate(LocalDateTime.now());
        }

        // 조회수 초기값 설정
        if (qaBoard.getReadnum() == null) {
            qaBoard.setReadnum(0);
        }
        return qaBoardRepository.save(qaBoard);
    }


    /**
     * 질문 수정
     * */
    @Override
    public QaBoard qaUpdate(Long boardNoSeq, QaBoard qaBoard) {

        QaBoard qa = qaBoardRepository.findById(boardNoSeq)
                .orElseThrow(()->new DMLException(ErrorCode.NOTFOUND_BOARD));

        qa.setSubject(qaBoard.getSubject());
        qa.setContent(qaBoard.getContent());

        return qaBoardRepository.save(qa);
    }


    /**
     * 질문 조회
     * */
    @Override
    @Transactional(readOnly = true)
    public QaBoard qaFindBySeq(Long boardNoSeq) {


        return qaBoardRepository.findById(boardNoSeq)
                .orElseThrow(()->new DMLException(ErrorCode.NOTFOUND_BOARD));
    }


    /**
     * 전체 조회
     * */
    @Override
    public List<QaBoard> qaFindAll() {


        return qaBoardRepository.findAll();
    }


    /**
     * 질문 삭제
     * */
    @Override
    public String qaDelete(Long boardNoSeq) {
        QaBoard qaBoard = qaBoardRepository.findById(boardNoSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));

        qaBoardRepository.delete(qaBoard);
        log.info("공지사항 삭제 성공: ID={}", boardNoSeq);

        return "정상적으로 삭제되었습니다.";
    }

    /**
     *  조회수 증가
     * */
    public QaBoard increaseReadnum(Long boardNoSeq) {
        QaBoard qaBoard = qaBoardRepository.findById(boardNoSeq)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        qaBoard.setReadnum(qaBoard.getReadnum() + 1);
        return qaBoardRepository.save(qaBoard);
    }










}
