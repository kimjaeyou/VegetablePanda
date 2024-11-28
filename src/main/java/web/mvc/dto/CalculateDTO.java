package web.mvc.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder

public class CalculateDTO {
    private List<CalcPoint> calculateDTO;

    public List<CalcPoint> getCalculateDTO() {
        return calculateDTO;
    }

    public void setCalculateDTO(List<CalcPoint> calculateDTO) {
        this.calculateDTO = calculateDTO;
    }
}
