package web.mvc.service;

import web.mvc.domain.Product;

import java.util.List;

public interface ProductService {

    /**
     * 상품 등록
     */
    public Product insertProduct(Product product);

    /**
     * 상품 조회
     */
    public List<Product> findAllProducts();

    /**
     * 상품 수정
     */
    public Product updateProduct(Product product);

    /**
     * 상품 삭제
     */
    public int deleteProduct(long id);
}
