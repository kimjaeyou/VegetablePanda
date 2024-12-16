package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.mvc.domain.ShopLike;
import web.mvc.dto.*;
import web.mvc.service.ShopService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ShopController {
    private final ShopService shopService;

    @GetMapping("/shop")
    public ResponseEntity<List<ShopListDTO>> getShopItems() {
        List<ShopListDTO> items = shopService.getAllShopItems(0);
        return ResponseEntity.ok(items);
    }

    // 윤성이가 씀
    @PostMapping("/shop/{seq}")
    public ResponseEntity<List<ShopListDTO>> getShopItemsUser(@PathVariable Long seq) {
        return new ResponseEntity <> (shopService.getShopItemsUser(seq) , HttpStatus.OK);
    }

    // 윤성이가 씀
    @PostMapping("/shop/select/{seq}")
    public ResponseEntity<List<ShopListDTO>> getShopItemsUserSelect(@PathVariable Long seq) {
        return new ResponseEntity <> (shopService.getAllShopItems(seq) , HttpStatus.OK);
    }

    @GetMapping("/daily")
    public ResponseEntity<List<SalesStatisticsDTO>> getDailySalesStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam Long stockSeq) {

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        return ResponseEntity.ok(shopService.getDailySalesStatistics(startDateTime, endDateTime, stockSeq));
    }

    @GetMapping("/weekly")
    public ResponseEntity<List<SalesStatisticsDTO>> getWeeklySalesStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam Long stockSeq) {

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        return ResponseEntity.ok(shopService.getWeeklySalesStatistics(startDateTime, endDateTime, stockSeq));
    }

    @GetMapping("/monthly")
    public ResponseEntity<List<SalesStatisticsDTO>> getMonthlySalesStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam Long stockSeq) {

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        return ResponseEntity.ok(shopService.getMonthlySalesStatistics(startDateTime, endDateTime, stockSeq));
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, List<SalesStatisticsDTO>>> getAllSalesStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam Long stockSeq) {

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        return ResponseEntity.ok(shopService.getAllSalesStatistics(startDateTime, endDateTime, stockSeq));
    }

    @PostMapping("/insertShopLike")
    public ResponseEntity<ShopLikeDTO> getShopItemsUser(@RequestBody ShopLikeSeqDTO shopLikeDTO) {
        System.out.println("찜하기"+shopLikeDTO.getUserSeq()+shopLikeDTO.getShopSeq());
        ShopLike shopLikeDo = shopService.getByUserSeqAndStockSeq(shopLikeDTO.getUserSeq(),shopLikeDTO.getShopSeq());
        if (shopLikeDo == null) {
            shopService.insertShopLike(shopLikeDTO.getUserSeq(),shopLikeDTO.getShopSeq());
            shopLikeDo = shopService.getByUserSeqAndStockSeq(shopLikeDTO.getUserSeq(),shopLikeDTO.getShopSeq());
        }
        ShopLikeDTO shopLike = ShopLikeDTO.builder()
                .shopSeq(shopLikeDo.getShop().getShopSeq())
                .userSeq(shopLikeDo.getManagementUser().getUserSeq())
                .state(shopLikeDo.getState())
                .build();
        return ResponseEntity.ok(shopLike);
    }

    @GetMapping("/getShopLike")
    public ResponseEntity<ShopLikeDTO> getShopLikeUser(@RequestParam Long userSeq, @RequestParam Long shopSeq) {
        ShopLike shopLikeDo = shopService.getShopLike(userSeq,shopSeq);
        ShopLikeDTO shopLike = ShopLikeDTO.builder()
                .shopSeq(shopLikeDo.getShop().getShopSeq())
                .userSeq(shopLikeDo.getManagementUser().getUserSeq())
                .state(shopLikeDo.getState())
                .build();

        return ResponseEntity.ok(shopLike);
    }

    @GetMapping("/price/statistics")
    public ResponseEntity<Map<String, Integer>> getPriceStatistics(@RequestParam Long stockSeq) {
        return ResponseEntity.ok(shopService.getPriceStatistics(stockSeq));
    }

    @GetMapping("/likes")
    public ResponseEntity<List<ShopLikeResponseDTO>> getLikedShops(@RequestParam Long userSeq) {
        List<ShopLikeResponseDTO> likedShops = shopService.getLikedShopsByUser(userSeq);
        return ResponseEntity.ok(likedShops);
    }

    @PostMapping("/shopItems/item/{seq}")
    public ResponseEntity<ShopListDTO> getRecItems(@PathVariable Long seq){
        return ResponseEntity.ok(shopService.findRecShop(seq));
    }
}