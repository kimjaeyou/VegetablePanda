package web.mvc.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "streaming")
@Data
public class Streaming {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "streaming_seq")
    private Integer streamingSeq;

    @Column(name = "token")
    private String token;

    @Column(name = "server_address")
    private String serverAddress;


    @OneToOne
    @JoinColumn(name = "farmer_seq", nullable = false)
    private FarmerUser farmerUser;
}
