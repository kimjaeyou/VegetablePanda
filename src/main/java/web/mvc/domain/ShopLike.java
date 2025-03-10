package web.mvc.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "shop_like")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class    ShopLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shop_like_seq")
    private Long shopLikeSeq;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_seq", nullable = false)
    private Shop shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private ManagementUser managementUser;

    @Column(name = "state")
    @ColumnDefault("true")
    private Boolean state;


}
