package web.mvc.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import web.mvc.dto.CartItemDTO;

import java.util.List;

public interface CartService {
    void addToCart(HttpServletRequest request, HttpServletResponse response,
              Long stockSeq, Integer quantity, Long userSeq);

    List<CartItemDTO> getCartItems(HttpServletRequest request, Long userSeq);

    void removeFromCart(HttpServletRequest request, HttpServletResponse response,
                        Long stockSeq, Long userSeq);

    void clearCart(HttpServletResponse response, Long userSeq);

    void updateQuantity(HttpServletRequest request, HttpServletResponse response,
                        Long stockSeq, Integer quantity, Long userSeq);

    int calculateTotal(List<CartItemDTO> cartItems);
}
