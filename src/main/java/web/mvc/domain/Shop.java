package web.mvc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "shop")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @CreationTimestamp
    private LocalDateTime insertDate;

    @JsonIgnore
    @OneToMany(mappedBy = "shop",fetch = FetchType.LAZY)
    private List<ShopLike> shopLikes;

    public void setStock(long stockSeq) {
        this.stock = new Stock(stockSeq);
    }
}
