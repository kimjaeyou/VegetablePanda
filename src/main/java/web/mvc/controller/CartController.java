package web.mvc.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.mvc.dto.CartItemDTO;
import web.mvc.service.CartServiceImpl;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartServiceImpl cartService;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @RequestParam Long stockSeq,
            @RequestParam Integer quantity,
            @RequestParam Long userSeq,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try {
            cartService.addToCart(request, response, stockSeq, quantity, userSeq);
            return ResponseEntity.ok().body(Map.of(
                    "message", "장바구니에 추가되었습니다.",
                    "cartItems", cartService.getCartItems(request, userSeq)
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<CartItemDTO>> getCart(
            @RequestParam Long userSeq,
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(cartService.getCartItems(request, userSeq));
    }

    @DeleteMapping("/{stockSeq}")
    public ResponseEntity<?> removeFromCart(
            @PathVariable Long stockSeq,
            @RequestParam Long userSeq,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        cartService.removeFromCart(request, response, stockSeq, userSeq);
        return ResponseEntity.ok(Map.of(
                "message", "상품이 장바구니에서 제거되었습니다.",
                "cartItems", cartService.getCartItems(request, userSeq)
        ));
    }

    @DeleteMapping
    public ResponseEntity<?> clearCart(
            @RequestParam Long userSeq,
            HttpServletResponse response
    ) {
        cartService.clearCart(response, userSeq);
        return ResponseEntity.ok("장바구니가 비워졌습니다.");
    }

    @PutMapping("/{stockSeq}")
    public ResponseEntity<?> updateQuantity(
            @PathVariable Long stockSeq,
            @RequestParam Integer quantity,
            @RequestParam Long userSeq,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try {
            cartService.updateQuantity(request, response, stockSeq, quantity, userSeq);
            return ResponseEntity.ok(Map.of(
                    "message", "수량이 업데이트되었습니다.",
                    "cartItems", cartService.getCartItems(request, userSeq)
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/total")
    public ResponseEntity<Integer> getCartTotal(
            @RequestParam Long userSeq,
            HttpServletRequest request
    ) {
        List<CartItemDTO> cartItems = cartService.getCartItems(request, userSeq);
        return ResponseEntity.ok(cartService.calculateTotal(cartItems));
    }
}