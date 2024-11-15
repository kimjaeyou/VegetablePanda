package web.mvc.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "stock_organic")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockOrganic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_organic")
    private Integer stockOrganicSeq;

    @Column(name = "oranic_status", nullable = false, length = 45)
    private String oranicStatus;

    @OneToMany(mappedBy = "stockOrganic",fetch = FetchType.LAZY)
    private List<Stock> stocks;

    public StockOrganic(int stockOrganicSeq) {
        this.stockOrganicSeq = stockOrganicSeq;
    }
}
