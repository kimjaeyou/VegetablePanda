package web.mvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.Shop;
import web.mvc.domain.ShopLike;
import web.mvc.domain.Stock;
import web.mvc.dto.*;
import web.mvc.repository.ShopLikeRepository;
import web.mvc.repository.ShopRepository;
import web.mvc.repository.UserBuyDetailRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;
    private final UserBuyDetailRepository userBuyDetailRepository;
    private final ShopLikeRepository shopLikeRepository;

    @Override
    public List<SalesStatisticsDTO> getDailySalesStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        return List.of();
    }

    @Transactional
    @Override
    public ShopLike getByUserSeqAndStockSeq(Long userSeq, Long shopSeq) {
         ShopLike shopLike = shopLikeRepository.findByUserSeqAndShopSeq(userSeq,shopSeq);
         if(shopLike!=null) {
             shopLike.setState(!shopLike.getState());
         }
         return shopLike;
    }

    @Transactional
    @Override
    public ShopLike getShopLike(Long userSeq, Long shopSeq) {
        ShopLike shopLike = shopLikeRepository.findByUserSeqAndShopSeq(userSeq,shopSeq);
        return shopLike;
    }

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
    public void insertShopLike(Long userSeq, Long shopSeq) {
         shopLikeRepository.insertShopLike(userSeq,shopSeq);
    }
    @Override
    public List<ShopListDTO> getAllShopItems(long seq) {
        List<ShopListDTO> items = new ArrayList<>();
        if(seq > 0){
            items = shopRepository.findByUserSeq(seq);
            log.info("item = {}",items);
        } else {
            items = shopRepository.findAllShopItems();
            log.info("조회된 상품 개수: {}", items.size());
            items.forEach(item -> log.info("상품 정보: {}", item));  // 각 상품 정보 출력
        }
        return items;
    }

    // 이것도 윤성이가 씀
    @Override
    public List<ShopListDTO> getShopItemsUser(long seq) {
        return shopRepository.findByUserSeq(seq);
    }

    private SalesStatisticsDTO convertToDTO(Object[] result) {
        return new SalesStatisticsDTO(
                (String) result[0],           // period (yyyy-MM-dd)
                (String) result[1],           // productName
                ((Number) result[2]).longValue(),    // totalSales
                ((Number) result[3]).longValue(),    // totalQuantity
                ((Number) result[4]).longValue(),    // totalAmount
                (String) result[5]            // periodType
        );
    }

    @Override
    public List<SalesStatisticsDTO> getDailySalesStatistics(LocalDateTime startDate, LocalDateTime endDate, Long stockSeq) {
        return shopRepository.findDailySalesStatistics(startDate, endDate, stockSeq)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SalesStatisticsDTO> getWeeklySalesStatistics(LocalDateTime startDate, LocalDateTime endDate, Long stockSeq) {
        return shopRepository.findWeeklySalesStatistics(startDate, endDate, stockSeq)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SalesStatisticsDTO> getMonthlySalesStatistics(LocalDateTime startDate, LocalDateTime endDate, Long stockSeq) {
        return shopRepository.findMonthlySalesStatistics(startDate, endDate, stockSeq)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<SalesStatisticsDTO>> getAllSalesStatistics(
            LocalDateTime startDate, LocalDateTime endDate, Long stockSeq) {
        Map<String, List<SalesStatisticsDTO>> statistics = new HashMap<>();

        statistics.put("daily", getDailySalesStatistics(startDate, endDate, stockSeq));
        statistics.put("weekly", getWeeklySalesStatistics(startDate, endDate, stockSeq));
        statistics.put("monthly", getMonthlySalesStatistics(startDate, endDate, stockSeq));

        return statistics;
    }

    @Override
    public Map<String, Integer> getPriceStatistics(Long stockSeq) {
        Map<String, Integer> priceStats = new HashMap<>();

        Integer yesterdayMax = shopRepository.findYesterdayMaxPrice(stockSeq);
        Integer weeklyAvg = shopRepository.findWeeklyAveragePrice(stockSeq);

        priceStats.put("yesterdayMaxPrice", yesterdayMax != null ? yesterdayMax : 0);
        priceStats.put("weeklyAveragePrice", weeklyAvg != null ? weeklyAvg : 0);

        return priceStats;
    }

    public List<ShopLikeResponseDTO> getLikedShopsByUser(Long userSeq) {
        return shopLikeRepository.findLikedShopsByUserSeq(userSeq);
    }
    @Override
    public ShopListDTO findRecShop(Long stockSeq){
        return shopRepository.findRecShop(stockSeq);
    }
}
