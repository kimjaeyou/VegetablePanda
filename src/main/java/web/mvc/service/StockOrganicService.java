package web.mvc.service;

import web.mvc.domain.StockOrganic;

import java.util.List;

public interface StockOrganicService {
    /**
     * 유기농 분류 조회
     */
    public List<StockOrganic> findAll();
}
