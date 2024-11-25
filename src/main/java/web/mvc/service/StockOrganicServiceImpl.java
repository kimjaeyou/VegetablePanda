package web.mvc.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.mvc.domain.StockOrganic;
import web.mvc.repository.StockOrganicRepository;

import java.util.List;

@Service
@Slf4j
@Transactional
public class StockOrganicServiceImpl implements StockOrganicService {

    @Autowired
    private StockOrganicRepository stockOrganicRepository;

    @Override
    public List<StockOrganic> findAll() {
        return stockOrganicRepository.findAll();
    }
}
