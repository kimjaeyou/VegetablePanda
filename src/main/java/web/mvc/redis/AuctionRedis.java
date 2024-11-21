package web.mvc.redis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@RedisHash("auction")
@RequiredArgsConstructor
public class AuctionRedis {

    @Id
    private Long auctionSeq;
    private int count;
    private String closeTime;
    private Integer status;

    private Long stockSeq;// Stock에 대한 참조 대신 stockSeq 사용
}