package web.mvc.dto;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;
import web.mvc.domain.FarmerUser;
@Getter
@Setter
@NoArgsConstructor
@Builder
@Data
@AllArgsConstructor
@ToString
public class StreamingDTO {
    private Long streamingSeq;

    private String token;

    private String serverAddress;

    private String chatUrl;

    private String chatRoomId;

    private String playbackUrl;

    private Integer state;

    private Long farmerSeq;

    private Long stockSeq;

    private String productName;
}
