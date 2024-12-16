package web.mvc.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.mvc.dto.CartItemDTO;
import web.mvc.service.CartService;
import web.mvc.service.CartServiceImpl;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @RequestParam Long stockSeq,
            @RequestParam Integer quantity,
            @RequestParam Long userSeq
    ) {
        try {
            CartItemDTO cartItem = cartService.addToCart(stockSeq, quantity, userSeq);
            return ResponseEntity.ok(cartItem);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/validate/{stockSeq}")
    public ResponseEntity<?> validateStock(
            @PathVariable Long stockSeq,
            @RequestParam Integer quantity
    ) {
        try {
            cartService.validateStock(stockSeq, quantity);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}