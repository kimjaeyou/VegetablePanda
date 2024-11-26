package web.mvc.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.mvc.domain.StockGrade;
import web.mvc.repository.StockGradeRepository;

import java.util.List;

@Service
@Slf4j
@Transactional
public class StockGradeServiceImpl implements StockGradeService {

    @Autowired
    private StockGradeRepository stockGradeRepository;

    @Override
    public List<StockGrade> findAll() {
        return stockGradeRepository.findAll();
    }
}
