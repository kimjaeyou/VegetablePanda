package web.mvc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.QaBoard;
import web.mvc.exception.DMLException;
import web.mvc.exception.ErrorCode;
import web.mvc.repository.QaBoardRepository;

import java.util.List;

@Service
@Transactional
@Slf4j
public class QaBoardServiceImpl implements QaBoardService {

    private QaBoardRepository qaBoardRepository;

    /**
     * 질문 등록
     * */
    @Override
    public QaBoard qaSave(QaBoard qaBoard) {


        return qaBoardRepository.save(qaBoard);
    }


    /**
     * 질문 수정
     * */
    @Override
    public QaBoard qaUpdate(Long boardNoSeq, QaBoard qaBoard) {

        QaBoard qa = qaBoardRepository.findById(boardNoSeq).orElseThrow(()->new DMLException(ErrorCode.NOTFOUND_BOARD));

        qa.setSubject(qaBoard.getSubject());
        qa.setContent(qaBoard.getContent());

        return qa;
    }


    /**
     * 질문 조회
     * */
    @Override
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
    public QaBoard qaDelete(Long boardNoSeq) {


        return null;
    }











}
