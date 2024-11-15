package web.mvc.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "stock_grade")
@Getter
@Setter
public class StockGrade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_grade_seq")
    private Integer productGradeSeq;

    @Column(name = "grade", nullable = false, length = 45)
    private String grade;

    @OneToMany(mappedBy = "stockGrade",fetch = FetchType.LAZY)
    private List<Stock> stocks;

}
