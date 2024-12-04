package web.mvc.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import web.mvc.domain.Shop;
import web.mvc.domain.Stock;
import web.mvc.dto.CartItemDTO;
import web.mvc.repository.ShopRepository;
import web.mvc.repository.StockRepository;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final StockRepository stockRepository;
    private final ShopRepository shopRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void addToCart(HttpServletRequest request, HttpServletResponse response, Long stockSeq, Integer quantity) {
        Stock stock = stockRepository.findById(stockSeq)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        Shop shop = shopRepository.findByStockSeq(stockSeq)
                .orElseThrow(() -> new RuntimeException("가격 정보를 찾을 수 없습니다."));

        if (stock.getStatus() != 3) {
            throw new RuntimeException("판매 가능한 상품이 아닙니다.");
        }

        if (stock.getCount() < quantity) {
            throw new RuntimeException("재고가 부족합니다.");
        }

        List<CartItemDTO> cart = getCartFromCookie(request);

        CartItemDTO existingItem = cart.stream()
                .filter(item -> item.getStockSeq().equals(stockSeq))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItemDTO newItem = CartItemDTO.builder()
                    .stockSeq(stock.getStockSeq())
                    .quantity(quantity)
                    .price(shop.getPrice())
                    .productName(stock.getProduct().getProductName())
                    .content(stock.getContent())
                    .imageUrl(stock.getFile() != null ? stock.getFile().getPath() : null)
                    .build();
            cart.add(newItem);
        }

        saveCartToCookie(response, cart);
    }

    private List<CartItemDTO> getCartFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("cart".equals(cookie.getName())) {
                    try {
                        String decodedValue = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                        return objectMapper.readValue(decodedValue,
                                new TypeReference<List<CartItemDTO>>() {});
                    } catch (Exception e) {
                        // 에러 시 빈 장바구니 반환
                        return new ArrayList<>();
                    }
                }
            }
        }
        return new ArrayList<>();
    }

    private void saveCartToCookie(HttpServletResponse response, List<CartItemDTO> cart) {
        try {
            String cartJson = objectMapper.writeValueAsString(cart);
            String encodedCart = URLEncoder.encode(cartJson, StandardCharsets.UTF_8);

            Cookie cookie = new Cookie("cart", encodedCart);
            cookie.setPath("/");
            cookie.setMaxAge(3 * 24 * 60 * 60);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
        } catch (Exception e) {
            throw new RuntimeException("장바구니 저장 실패", e);
        }
    }

    @Override
    public List<CartItemDTO> getCartItems(HttpServletRequest request) {
        return getCartFromCookie(request);
    }

    @Override
    public void removeFromCart(HttpServletRequest request, HttpServletResponse response, Long stockSeq) {
        List<CartItemDTO> cart = getCartFromCookie(request);
        cart.removeIf(item -> item.getStockSeq().equals(stockSeq));
        saveCartToCookie(response, cart);
    }

    @Override
    public void clearCart(HttpServletResponse response) {
        Cookie cookie = new Cookie("cart", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    @Override
    public void updateQuantity(HttpServletRequest request, HttpServletResponse response,
                               Long stockSeq, Integer quantity) {
        Stock stock = stockRepository.findById(stockSeq)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        if (stock.getCount() < quantity) {
            throw new RuntimeException("재고가 부족합니다.");
        }

        List<CartItemDTO> cart = getCartFromCookie(request);
        cart.stream()
                .filter(item -> item.getStockSeq().equals(stockSeq))
                .findFirst()
                .ifPresent(item -> item.setQuantity(quantity));

        saveCartToCookie(response, cart);
    }

    @Override
    public int calculateTotal(List<CartItemDTO> cartItems) {
        return cartItems.stream()
                .mapToInt(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}