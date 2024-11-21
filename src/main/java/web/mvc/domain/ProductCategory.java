package web.mvc.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "product_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_category_seq")
    private Long productCategorySeq;

    @Column(name = "content", nullable = false, length = 45)
    private String content;

    @OneToMany(mappedBy = "productCategory",fetch = FetchType.LAZY)
    private List<Product> products;

    public ProductCategory(Long productCategorySeq) {
        this.productCategorySeq = productCategorySeq;
    }
}
