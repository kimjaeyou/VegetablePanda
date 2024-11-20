package web.mvc.service;

import org.springframework.http.ResponseEntity;
import web.mvc.domain.QaBoard;

import java.util.List;

public interface QaBoardService {

    /**
     * QA 등록
     * */
    public QaBoard qaSave();

    /**
     * QA 수정
     * */
    public QaBoard qaUpdate();

    /**
     * QA 조회
     * */
    public QaBoard qaFindBySeq();

    /**
     * 전체 조회
     * */
    public List<QaBoard> qaFindAll();

    /**
     * QA 삭제
     * */
    public QaBoard qaDelete();
}
