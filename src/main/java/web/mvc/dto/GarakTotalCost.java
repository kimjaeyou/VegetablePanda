package web.mvc.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GarakTotalCost {
    private String garak_name;
    private int garak_price;
    private long garak_type;
    private long garak_grade;
    private long garak_category;
}
