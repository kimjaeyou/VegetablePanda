package web.mvc.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "product_organic")
@Data
public class ProductOrganic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_organic")
    private Integer productOrganic;

    @Column(name = "oranic_status", nullable = false, length = 45)
    private String oranicStatus;

    @OneToMany(mappedBy = "productOrganic",fetch = FetchType.LAZY)
    private List<Product> products;
}
