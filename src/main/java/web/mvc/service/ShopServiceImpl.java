package web.mvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import web.mvc.domain.Shop;
import web.mvc.domain.Stock;
import web.mvc.dto.ShopListDTO;
import web.mvc.dto.StockDTO;
import web.mvc.repository.ShopRepository;
import web.mvc.repository.UserBuyDetailRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;
    private final UserBuyDetailRepository userBuyDetailRepository;

    public int shopInsert(StockDTO stock) {
        Shop shop = new Shop();
        shop.setInsertDate(LocalDateTime.now());
        shop.setPrice(10000);
        shop.setStock(stock.getStockSeq());
        shop=shopRepository.save(shop);
        return 0;
    }

    @Override
    public int shopUpdate(Shop shop) {
        Optional<Shop> findShop = shopRepository.findById(shop.getShopSeq());
        Shop fshop=findShop.get();
        fshop.setPrice(shop.getPrice());
        shopRepository.save(fshop);
        return 0;
    }

    @Override
    public int shopDelete(Long code) {
        return 0;
    }

    @Override
    public List<ShopListDTO> getAllShopItems() {
        List<ShopListDTO> items = shopRepository.findAllShopItems();
        log.info("조회된 상품 개수: {}", items.size());
        items.forEach(item -> log.info("상품 정보: {}", item));  // 각 상품 정보 출력
        return items;
    }
}
