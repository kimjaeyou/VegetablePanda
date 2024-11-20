package web.mvc.service;

import org.springframework.http.ResponseEntity;
import web.mvc.domain.Review;

import java.util.List;

public interface ReviewService {

    /**
     * 리뷰 등록
     * */
    public Review reviewSave();

    /**
     * 리뷰 수정
     * */
    public Review reviewUpdate();

    /**
     * 리뷰 조회
     * */
    public Review reviewFindBySeq();

    /**
     * 전체 조회
     * */
    public List<Review> reviewFindAll();

    /**
     * 리뷰 삭제
     * */
    public Review reviewDelete();
}
