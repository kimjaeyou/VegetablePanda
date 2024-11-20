package web.mvc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.QaBoard;

import java.util.List;

@Service
@Transactional
@Slf4j
public class QaBoardServiceImpl implements QaBoardService {


    /**
     * 질문 등록
     * */
    @Override
    public QaBoard qaSave() {
        return null;
    }


    /**
     * 질문 조회
     * */
    @Override
    public QaBoard qaUpdate() {
        return null;
    }


    /**
     * 질문 수정
     * */
    @Override
    public QaBoard qaFindBySeq() {
        return null;
    }


    /**
     * 전체 조회
     * */
    @Override
    public List<QaBoard> qaFindAll() {
        return null;
    }


    /**
     * 질문 삭제
     * */
    @Override
    public QaBoard qaDelete() {
        return null;
    }











}
