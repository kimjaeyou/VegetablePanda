package web.mvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import web.mvc.domain.Shop;
import web.mvc.domain.Stock;
import web.mvc.dto.StockDTO;
import web.mvc.repository.ShopRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;

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
}
