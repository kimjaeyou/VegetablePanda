package web.mvc.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "product")
@Data
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

}
