package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.mvc.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
