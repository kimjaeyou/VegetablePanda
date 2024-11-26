package web.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendUserDTO {
    private static final long serialVersionUID = 1L;
    long userSeq;
    long farmerSeq;
    int score;
}
