package web.mvc.service;

import org.springframework.stereotype.Service;
import web.mvc.dto.ShopListDTO;

import java.util.List;

@Service
public interface ShopService {
    List<ShopListDTO> getAllShopItems();
}
