package web.mvc.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import web.mvc.dto.CartItemDTO;

import java.util.List;

public interface CartService {
    void addToCart(HttpServletRequest request, HttpServletResponse response, Long stockSeq, Integer quantity);

    List<CartItemDTO> getCartItems(HttpServletRequest request);

    void removeFromCart(HttpServletRequest request, HttpServletResponse response, Long stockSeq);

    void clearCart(HttpServletResponse response);

    void updateQuantity(HttpServletRequest request, HttpServletResponse response,
                        Long stockSeq, Integer quantity);

    int calculateTotal(List<CartItemDTO> cartItems);
}
