package web.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class OrderByBuyCountDTO {
    private Long userSeq;
    private Long buyCount;

    public OrderByBuyCountDTO(Long userSeq, Long buyCount) {
        this.userSeq = userSeq;
        this.buyCount = buyCount;
    }

    // Getter & Setter
    public Long getUserSeq() {
        return userSeq;
    }

    public void setUserSeq(Long userSeq) {
        this.userSeq = userSeq;
    }

    public Long getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Long buyCount) {
        this.buyCount = buyCount;
    }
}
