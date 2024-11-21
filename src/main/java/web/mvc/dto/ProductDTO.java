package web.mvc.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import web.mvc.domain.ProductCategory;

@NoArgsConstructor
@Getter
@Setter
public class ProductDTO {
    private long productSeq;
    private String productName;
    private int productCategorySeq;

}
