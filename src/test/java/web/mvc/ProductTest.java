package web.mvc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import web.mvc.repository.ProductRepository;

@SpringBootTest
@Rollback(false)
@Slf4j
public class ProductTest {

    ProductRepository productRepository;
}
