package web.mvc.service;

import web.mvc.domain.ProductCategory;

import java.util.List;

public interface ProductCategoryService {
    /**
     * 상품 카테고리 조회
     */
    public List<ProductCategory> findAll();
}
