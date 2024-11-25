package web.mvc.service;

import web.mvc.domain.QaBoard;

import java.util.List;

public interface QaBoardService {

    /**
     * QA 등록
     * */
    public QaBoard qaSave(QaBoard qaBoard);

    /**
     * QA 수정
     * */
    public QaBoard qaUpdate(Long boardNoSeq, QaBoard qaBoard);

    /**
     * QA 조회
     * */
    public QaBoard qaFindBySeq(Long boardNoSeq);

    /**
     * 전체 조회
     * */
    public List<QaBoard> qaFindAll();

    /**
     * QA 삭제
     * */
    public String qaDelete(Long boardNoSeq);
}
