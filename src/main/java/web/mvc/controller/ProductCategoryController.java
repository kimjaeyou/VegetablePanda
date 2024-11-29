package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import web.mvc.domain.ProductCategory;
import web.mvc.dto.ProductCategoryDTO;
import web.mvc.dto.ProductDTO;
import web.mvc.service.ProductCategoryService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ProductCategoryController {

    private final ModelMapper modelMapper;
    private final ProductCategoryService productCategoryService;

    // 상품 카테고리 조회
    @GetMapping("/category")
    public ResponseEntity<?> getAllProductCategory () {
        List<ProductCategory> productCategoryList = productCategoryService.findAll();
        List<ProductCategoryDTO> productCategoryDTOList = productCategoryList.stream().map(data -> modelMapper.map(data, ProductCategoryDTO.class)).toList();
        return new ResponseEntity<>(productCategoryDTOList, HttpStatus.OK);
    }
}
