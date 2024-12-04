package web.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopLikeDTO {

    private Long shopSeq;
    private Long userSeq;
    private Boolean liked;


}
