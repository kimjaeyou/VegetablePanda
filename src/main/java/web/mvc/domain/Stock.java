package web.mvc.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import java.util.List;

@Entity
@Table(name = "stock")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_seq")
    private long stockSeq;

    @Column(name = "content", nullable = false, length = 45)
    private String content;

    @Column(name = "count")
    private Integer count;

    @Column(name = "status")
    private Integer status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_product_seq", nullable = false)
    private Product product;

    @OneToMany(mappedBy = "stock")
    private List<UserBuyDetail> userBuyDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_grade_seq", nullable = false)
    private StockGrade stockGrade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_organic_seq", nullable = false)
    private StockOrganic stockOrganic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private FarmerUser farmerUser;

    @OneToOne
    @JoinColumn(name = "file_seq")
    private File file;

    public Stock(Long stockSeq){
        this.stockSeq = stockSeq;
    }

    private int color;


}
