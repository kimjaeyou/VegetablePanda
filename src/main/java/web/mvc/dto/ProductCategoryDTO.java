package web.mvc.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ProductCategoryDTO {
    private int productCategorySeq;
    private String content;
    private List<ProductDTO> products;
}
