package web.mvc.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "shop")
@Data
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shop_seq")
    private Integer shopSeq;

    @OneToOne
    @JoinColumn(name = "stock_seq", nullable = false)
    private Stock stock;

    @Column(name = "price")
    private Integer price;

    @Column(name = "insert_date", length = 60)
    private String insertDate;
}
