package web.mvc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.Review;

import java.util.List;

@Service
@Transactional
@Slf4j
public class ReviewServiceImpl implements ReviewService {



    /**
     *  리뷰 등록
     * */
    @Override
    public Review reviewSave() {
        return null;
    }


    /**
     *  리뷰 조회
     * */
    @Override
    public Review reviewUpdate() {
        return null;
    }


    /**
     *  리뷰 수정
     * */
    @Override
    public Review reviewFindBySeq() {
        return null;
    }


    /**
     * 전체조회
     * */
    @Override
    public List<Review> reviewFindAll() {
        return null;
    }


    /**
     *  리뷰 삭제
     * */
    @Override
    public Review reviewDelete() {
        return null;
    }












}
