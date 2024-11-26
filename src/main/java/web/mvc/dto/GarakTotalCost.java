package web.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.hadoop.shaded.org.checkerframework.checker.units.qual.A;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GarakTotalCost {
    private String garak_name;
    private int garak_price;
    private String garak_type;
    private String garak_grade;
    private long garak_category;
}
