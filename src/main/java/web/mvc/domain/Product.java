package web.mvc.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_seq")
    private Integer productSeq;

    @Column(name = "product_name", length = 45)
    private String productName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_category_product_category_seq", nullable = false)
    private ProductCategory productCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_grade_product_grade_seq", nullable = false)
    private ProductGrade productGrade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_organic_product_organic", nullable = false)
    private ProductOrganic productOrganic;

    public Product(int productSeq){
        this.productSeq = productSeq;
    }
}
