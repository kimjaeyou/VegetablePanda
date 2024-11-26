package web.mvc.service;

import web.mvc.domain.StockGrade;

import java.util.List;

public interface StockGradeService {
    /**
     * 등급 분류 조회
     */
    public List<StockGrade> findAll();
}
