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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final StockRepository stockRepository;
    private final ShopRepository shopRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void addToCart(HttpServletRequest request, HttpServletResponse response,
                          Long stockSeq, Integer quantity, Long userSeq) {
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

        List<CartItemDTO> cart = getCartFromCookie(request, userSeq);

        CartItemDTO existingItem = cart.stream()
                .filter(item -> item.getStockSeq().equals(stockSeq))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItemDTO newItem = CartItemDTO.builder()
                    .userSeq(userSeq)
                    .stockSeq(stock.getStockSeq())
                    .quantity(quantity)
                    .price(shop.getPrice())
                    .productName(stock.getProduct().getProductName())
                    .content(stock.getContent())
                    .imageUrl(stock.getFile() != null ? stock.getFile().getPath() : null)
                    .build();
            cart.add(newItem);
        }

        saveCartToCookie(response, cart, userSeq);
    }

    private List<CartItemDTO> getCartFromCookie(HttpServletRequest request, Long userSeq) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (("cart_" + userSeq).equals(cookie.getName())) {  // 쿠키 이름에 userSeq 추가
                    try {
                        String decodedValue = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                        List<CartItemDTO> cartItems = objectMapper.readValue(decodedValue,
                                new TypeReference<List<CartItemDTO>>() {});
                        return cartItems.stream()
                                .filter(item -> item.getUserSeq().equals(userSeq))
                                .collect(Collectors.toList());
                    } catch (Exception e) {
                        System.err.println("쿠키 파싱 실패: " + e.getMessage());
                        e.printStackTrace();
                        return new ArrayList<>();
                    }
                }
            }
        }
        return new ArrayList<>();
    }

    private void saveCartToCookie(HttpServletResponse response, List<CartItemDTO> cart, Long userSeq) {
        try {
            String cartJson = objectMapper.writeValueAsString(cart);
            String encodedCart = URLEncoder.encode(cartJson, StandardCharsets.UTF_8);

            Cookie cookie = new Cookie("cart_" + userSeq, encodedCart);  // 쿠키 이름에 userSeq 추가
            cookie.setPath("/");
            cookie.setMaxAge(3 * 24 * 60 * 60);
            cookie.setDomain("");
            cookie.setSecure(true);
            cookie.setHttpOnly(false);

            response.addCookie(cookie);
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
//            response.setHeader("Access-Control-Allow-Origin", "https://vegetablepanda.p-e.kr");
        } catch (Exception e) {
            throw new RuntimeException("장바구니 저장 실패", e);
        }
    }

    @Override
    public List<CartItemDTO> getCartItems(HttpServletRequest request, Long userSeq) {
        return getCartFromCookie(request, userSeq);
    }

    @Override
    public void removeFromCart(HttpServletRequest request, HttpServletResponse response,
                               Long stockSeq, Long userSeq) {
        List<CartItemDTO> cart = getCartFromCookie(request, userSeq);
        cart.removeIf(item -> item.getStockSeq().equals(stockSeq)
                && item.getUserSeq().equals(userSeq));
        saveCartToCookie(response, cart, userSeq);
    }

    @Override
    public void clearCart(HttpServletResponse response, Long userSeq) {
        Cookie cookie = new Cookie("cart_" + userSeq, null);  // 쿠키 이름에 userSeq 추가
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    @Override
    public void updateQuantity(HttpServletRequest request, HttpServletResponse response,
                               Long stockSeq, Integer quantity, Long userSeq) {
        Stock stock = stockRepository.findById(stockSeq)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        if (stock.getCount() < quantity) {
            throw new RuntimeException("재고가 부족합니다.");
        }

        List<CartItemDTO> cart = getCartFromCookie(request, userSeq);
        cart.stream()
                .filter(item -> item.getStockSeq().equals(stockSeq)
                        && item.getUserSeq().equals(userSeq))
                .findFirst()
                .ifPresent(item -> item.setQuantity(quantity));

        saveCartToCookie(response, cart, userSeq);
    }

    @Override
    public int calculateTotal(List<CartItemDTO> cartItems) {
        return cartItems.stream()
                .mapToInt(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}