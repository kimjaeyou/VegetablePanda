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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_user_seq", nullable = false)
    private FarmerUser farmerUser;

}
