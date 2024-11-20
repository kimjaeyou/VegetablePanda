package web.mvc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import web.mvc.domain.Product;
import web.mvc.domain.ProductCategory;
import web.mvc.repository.ProductCategoryRepository;
import web.mvc.repository.ProductRepository;

@SpringBootTest
@Rollback(false)
@Slf4j
public class ProductTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    /**
     * 상품 카테고리 등록
     */
    @Test
    @Rollback(false)
    public void productCategoryInsert(){
        productCategoryRepository.save(ProductCategory.builder().content("식량작물").build());
        productCategoryRepository.save(ProductCategory.builder().content("엽채류").build());
        productCategoryRepository.save(ProductCategory.builder().content("과채류").build());
        productCategoryRepository.save(ProductCategory.builder().content("근채류").build());
        productCategoryRepository.save(ProductCategory.builder().content("과수").build());
        productCategoryRepository.save(ProductCategory.builder().content("양채류").build());
        productCategoryRepository.save(ProductCategory.builder().content("기타작물").build());
    }

    /**
     * 상품 샘플 등록
     */
    @Test
    @Rollback(false)
    public void productInsert(){
        productRepository.save(Product.builder().productCategory(new ProductCategory(4)).productName("감자").build());
        productRepository.save(Product.builder().productCategory(new ProductCategory(5)).productName("고구마").build());
        productRepository.save(Product.builder().productCategory(new ProductCategory(1)).productName("쌀").build());
        productRepository.save(Product.builder().productCategory(new ProductCategory(2)).productName("상추").build());
    }
}
