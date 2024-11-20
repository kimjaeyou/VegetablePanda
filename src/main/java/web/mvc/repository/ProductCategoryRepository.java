package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.mvc.domain.ProductCategory;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
}
