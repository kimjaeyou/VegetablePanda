package web.mvc.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
@Data
@AllArgsConstructor
@ToString
public class AuctionDTO implements Serializable {

    private Long auctionSeq;
    private int count;

    private String startTime;

    private String closeTime;

    private int status;

    private Long stockSeq;
}
