package web.mvc.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "product_grade")
@Data
public class ProductGrade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_grade_seq")
    private Integer productGradeSeq;

    @Column(name = "grade", nullable = false, length = 45)
    private String grade;

    @OneToMany(mappedBy = "productGrade",fetch = FetchType.LAZY)
    private List<Product> products;
}
