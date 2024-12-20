package web.mvc.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "streaming")
@Getter
@Setter
public class Streaming {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "streaming_seq")
    private Long streamingSeq;

    @Column(name = "token")
    private String token;

    @Column(name = "server_address")
    private String serverAddress;

    //state까지 추가한 부분
    @Column(name = "chat_api_url")
    private String chatUrl;

    @Column(name = "chat_room_id")
    private String chatRoomId;
    @Column(name = "PLAYBACK_URL")
    private String playbackUrl;

    @Column(name = "state")
    private Integer state;

    @OneToOne(fetch = FetchType.EAGER)  // EAGER로 변경
    @JoinColumn(name = "user_seq")
    private FarmerUser farmerUser;
}
