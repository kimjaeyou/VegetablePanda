package web.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GarakDTO {
    private String garak_name;
    private double garak_price;
    private int garak_count;
    private String garak_type;
    private String garak_grade;
}
