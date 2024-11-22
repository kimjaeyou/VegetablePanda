package web.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HighestBidDTO {
     Long auctionSeq;

     Long userSeq;

     int price;

}
