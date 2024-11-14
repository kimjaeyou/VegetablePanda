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
    
    //state까지 추가한 부분
    @Column(name = "chat_api_url")
    private String chatUrl;

    @Column(name = "chat_room_id")
    private String chatRoomId;

    @Column(name = "state")
    private Integer state;
}
