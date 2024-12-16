package web.mvc.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import web.mvc.dto.CartItemDTO;

import java.util.List;

public interface CartService {
    CartItemDTO addToCart(Long stockSeq, Integer quantity, Long userSeq);

    void validateStock(Long stockSeq, Integer quantity);
}
