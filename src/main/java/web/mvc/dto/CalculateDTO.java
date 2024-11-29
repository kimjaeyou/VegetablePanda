package web.mvc.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder

public class CalculateDTO {
    private List<CalcPoint2> calculateDTO;
}
