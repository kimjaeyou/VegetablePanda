package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.mvc.dto.ShopListDTO;
import web.mvc.service.ShopService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ShopController {
    private final ShopService shopService;

    @GetMapping("/api/shop")
    public ResponseEntity<List<ShopListDTO>> getShopItems() {
        List<ShopListDTO> items = shopService.getAllShopItems(0);
        return ResponseEntity.ok(items);
    }

    @PostMapping("/api/shop")
    public ResponseEntity<List<ShopListDTO>> getShopItemsUser(@RequestBody long userSeq) {
        List<ShopListDTO> items = shopService.getAllShopItems(userSeq);
        return ResponseEntity.ok(items);
    }

}