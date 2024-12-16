package web.mvc.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CalculateDTO {
    private List<CalcPoint2> calculateDTO;

    public List<CalcPoint2> getCalculateDTO() {
        return calculateDTO;
    }

    public void setSettlements(List<CalcPoint2> calculateDTO) {
        this.calculateDTO = calculateDTO;
    }
}
