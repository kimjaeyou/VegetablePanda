package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.mvc.domain.Shop;
import web.mvc.domain.Stock;
import web.mvc.dto.StockDTO;
import web.mvc.service.ShopService;

@RestController
@RequestMapping("/shop")
public class ShopBuyController {

    @Autowired
    private ShopService shopService;

    @PostMapping("/add")
    public void shopInsert(@RequestBody StockDTO stock) {
        shopService.shopInsert(stock);
    }

    @PostMapping("/{code}")
    public void shopUpdate(@RequestBody Shop shop) {
        shopService.shopUpdate(shop);
    }


    @GetMapping("/{code}")
    public void shopDelete(@PathVariable Long code) {
        shopService.shopDelete(code);
    }


}
