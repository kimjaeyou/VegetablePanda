package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.mvc.domain.Product;
import web.mvc.domain.ProductCategory;
import web.mvc.dto.ProductDTO;
import web.mvc.service.ProductService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ModelMapper modelMapper;
    private final ProductService productService;

    // 상품 등록
    @PostMapping("/product")
    public ResponseEntity<?> insertProduct (@RequestBody ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
        ProductDTO result = modelMapper.map(productService.insertProduct(product), ProductDTO.class);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    // 상품 목록 조회
    @GetMapping("/product")
    public ResponseEntity<?> getAllProducts() {
        List<Product> productList = productService.findAllProducts();
        List<ProductDTO> productDTOList = productList.stream().map(data -> modelMapper.map(data, ProductDTO.class)).toList();
        return new ResponseEntity<>(productDTOList, HttpStatus.OK);
    }

    // 카테고리 분류에 따른 상품 목록 조회
    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProductsByCategory(@PathVariable long id) {
        List<Product> productList = productService.findProductsByCategory(id);
        List<ProductDTO> productDTOList = productList.stream().map(data -> modelMapper.map(data, ProductDTO.class)).toList();
        return new ResponseEntity<>(productDTOList, HttpStatus.OK);

    }

    // 상품 수정
    @PutMapping("/product/{productSeq}")
    public ResponseEntity<?> updateProduct (@PathVariable long productSeq, @RequestBody ProductDTO productDTO) {
        log.info("{} 번 재고 정보 수정", productSeq);
        log.info("Update Product : {}", productDTO);

        Product product = modelMapper.map(productDTO, Product.class);
        ProductDTO result = modelMapper.map(productService.updateProduct(product), ProductDTO.class);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 상품 삭제
    @DeleteMapping("/product/{id}")
    public ResponseEntity<?> deleteProduct (@PathVariable long id) {
        log.info("Delete Product id : {}", id);
        productService.deleteProduct(id);
        return new ResponseEntity<>(id+"번 상품 삭제 완료", HttpStatus.OK);
    }

}
