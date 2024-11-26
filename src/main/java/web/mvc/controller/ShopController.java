package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import web.mvc.dto.ShopListDTO;
import web.mvc.service.ShopService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ShopController {
    private final ShopService shopService;

    @GetMapping("/api/shop")
    public ResponseEntity<List<ShopListDTO>> getShopItems() {
        List<ShopListDTO> items = shopService.getAllShopItems();
        return ResponseEntity.ok(items);
    }

}