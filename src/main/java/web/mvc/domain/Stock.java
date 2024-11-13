package web.mvc.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "stock")
@Data
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_seq")
    private Integer stockSeq;

    @Column(name = "content", nullable = false, length = 45)
    private String content;

    @Column(name = "count")
    private Integer count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_product_seq", nullable = false)
    private Product product;

    @OneToMany(mappedBy = "stock")
    private List<UserBuy> userBuys;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_grade_seq", nullable = false)
    private StockGrade stockGrade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_organic_seq", nullable = false)
    private StockOrganic stockOrganic;

}
