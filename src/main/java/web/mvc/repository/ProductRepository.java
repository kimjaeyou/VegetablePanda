package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.Product;
import web.mvc.domain.ProductCategory;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where p.productCategory.productCategorySeq = ?1")
    public List<Product> findByProductCategory(long productCategorySeq);
}
