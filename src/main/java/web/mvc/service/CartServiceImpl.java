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
    public CartItemDTO addToCart(Long stockSeq, Integer quantity, Long userSeq) {
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

        // 장바구니에 추가할 상품 정보 반환
        return CartItemDTO.builder()
                .userSeq(userSeq)
                .stockSeq(stock.getStockSeq())
                .quantity(quantity)
                .price(shop.getPrice())
                .productName(stock.getProduct().getProductName())
                .content(stock.getContent())
                .imageUrl(stock.getFile() != null ? stock.getFile().getPath() : null)
                .build();
    }
    @Override
    public void validateStock(Long stockSeq, Integer quantity) {
        Stock stock = stockRepository.findById(stockSeq)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        if (stock.getCount() < quantity) {
            throw new RuntimeException("재고가 부족합니다.");
        }
    }
}