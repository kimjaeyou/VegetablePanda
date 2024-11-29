package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.UserBuy;
import web.mvc.dto.UserBuyListByStockDTO;

import java.util.List;

public interface UserBuyRepository extends JpaRepository<UserBuy, Long> {

    @Query("select new web.mvc.dto.UserBuyListByStockDTO(d.price, d.count, b.buyDate, d.stock.product.productName, d.stock.product.productCategory.content, d.stock.stockOrganic.organicStatus, d.stock.stockGrade.grade) from UserBuyDetail d join UserBuy b on b.buySeq = d.userBuySeq where d.stock.stockSeq = ?1 order by b.buyDate DESC ")
    public List<UserBuyListByStockDTO> findByStockSeq(Long stockSeq);
}
