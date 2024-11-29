package web.mvc.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CalcPoint2 {
    private int state;
    private int totalPoint;
    private String insertDate;
    private Long userSeq; // JSON의 "user_seq"와 매핑

}
