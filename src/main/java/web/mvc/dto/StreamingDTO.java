package web.mvc.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
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
    private String farmerName;
    private String filePath;
}