package web.mvc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.Product;
import web.mvc.exception.ErrorCode;
import web.mvc.exception.ProductException;
import web.mvc.repository.ProductRepository;

import java.util.List;

@Service
@Slf4j
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Override
    public Product insertProduct(Product product) {

        return productRepository.save(product);
    }

    @Override
    public List<Product> findAllProducts() {

        return productRepository.findAll();
    }

    @Override
    public Product updateProduct(Product product) {
        Product dbProduct = productRepository.findById(product.getProductSeq()).orElseThrow(()-> new ProductException(ErrorCode.PRODUCT_UPDATE_FAILED));

        dbProduct.setProductCategory(product.getProductCategory());
        dbProduct.setProductName(product.getProductName());
        dbProduct.setProductCategory(product.getProductCategory());

        return dbProduct;
    }

    @Override
    public int deleteProduct(int id) {
         Product product = productRepository.findById(id).orElseThrow(()-> new ProductException(ErrorCode.PRODUCT_NOTFOUND));
         productRepository.delete(product);
        return 1;
    }
}
