package web.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GarakTotalCost {
    private String garak_name;
    private int garak_price;
    private long garak_type;
    private long garak_grade;
    private long garak_category;
}
