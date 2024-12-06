package web.mvc.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CalcPoint2 {
    private int totalPoint;
    private Long userBuySeq;
    private Integer state;
}
