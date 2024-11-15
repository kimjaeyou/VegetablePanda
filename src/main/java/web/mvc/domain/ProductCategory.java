package web.mvc.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "product_category")
@Getter
@Setter
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_category_seq")
    private Integer productCategorySeq;

    @Column(name = "content", nullable = false, length = 45)
    private String content;

    @OneToMany(mappedBy = "productCategory",fetch = FetchType.LAZY)
    private List<Product> products;
}
