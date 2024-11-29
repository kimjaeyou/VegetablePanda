package web.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import web.mvc.domain.Likes;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeDTO {
    Long userSeq;
    Long farmerSeq;

    public static Likes LikeInvert(Long userSeq, Long farmerSeq) {
        return new Likes(userSeq,farmerSeq);
    }
}
