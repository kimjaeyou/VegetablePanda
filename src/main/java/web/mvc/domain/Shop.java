package web.mvc.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "shop")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shop_seq")
    private Long shopSeq;

    @OneToOne
    @JoinColumn(name = "stock_seq")
    private Stock stock;

    @Column(name = "price")
    private Integer price;

    @Column(name = "insert_date", length = 60)
    private LocalDateTime insertDate;


    public void setStock(long stockSeq) {
        this.stock = new Stock(stockSeq);
    }
}
