package web.mvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import web.mvc.dto.ShopListDTO;
import web.mvc.repository.ShopRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {
    private final ShopRepository shopRepository;

    @Override
    public List<ShopListDTO> getAllShopItems() {
        return shopRepository.findAllShopItems();
    }
}