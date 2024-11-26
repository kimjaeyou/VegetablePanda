package web.mvc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.ProductCategory;
import web.mvc.repository.ProductCategoryRepository;

import java.util.List;

@Transactional
@Service
@Slf4j
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    ProductCategoryRepository repository;

    @Override
    public List<ProductCategory> findAll() {

        return repository.findAll();
    }
}
