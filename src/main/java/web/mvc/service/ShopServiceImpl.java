package web.mvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import web.mvc.dto.ShopListDTO;
import web.mvc.repository.ShopRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShopServiceImpl implements ShopService {
    private final ShopRepository shopRepository;

    @Override
    public List<ShopListDTO> getAllShopItems() {
        List<ShopListDTO> items = shopRepository.findAllShopItems();
        log.info("조회된 상품 개수: {}", items.size());
        items.forEach(item -> log.info("상품 정보: {}", item));  // 각 상품 정보 출력
        return items;
    }
}