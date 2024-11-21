package web.mvc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import web.mvc.domain.*;
import web.mvc.repository.*;

@SpringBootTest
@Rollback(false)
@Slf4j
public class ProductTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private ManagementRepository managementRepository;
    @Autowired
    private FarmerUserRepository farmerUserRepository;
    @Autowired
    private NormalUserRepository normalUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 유저 등록(farmer)
     */
    @Test
    @Rollback(false)
    @Disabled
    public void singupTest(){
        managementRepository.save(ManagementUser.builder().content("farmer").id("farmer").build());
        farmerUserRepository.save(FarmerUser.builder().farmerId("farmer").name("farmer").pw(passwordEncoder.encode("1234")).address("Ori").code("code").account("111-1111").phone("000-1111-2222").email("farmer@gmail.com").role("ROLE_FARMER").build());

        managementRepository.save(ManagementUser.builder().content("user").id("user").build());
        normalUserRepository.save(User.builder().id("user").name("user").pw(passwordEncoder.encode("1234")).address("Ori").phone("000-1111-2222").state(0).email("user@gmail.com").gender("M").role("ROLE_USER").build());
    }

    /**
     * 상품 카테고리 등록
     */
    @Test
    @Rollback(false)
    @Disabled
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
    @Disabled
    public void productInsert(){
        productRepository.save(Product.builder().productCategory(new ProductCategory(4)).productName("감자").build());
        productRepository.save(Product.builder().productCategory(new ProductCategory(5)).productName("고구마").build());
        productRepository.save(Product.builder().productCategory(new ProductCategory(1)).productName("쌀").build());
        productRepository.save(Product.builder().productCategory(new ProductCategory(2)).productName("상추").build());
    }
}
