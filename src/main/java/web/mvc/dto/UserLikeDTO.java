package web.mvc.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class UserLikeDTO {
    Long userSeq;
    Long farmerSeq;
    String path;
    String name;
}
