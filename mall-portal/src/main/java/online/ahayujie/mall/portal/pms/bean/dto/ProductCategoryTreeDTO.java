package online.ahayujie.mall.portal.pms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import online.ahayujie.mall.portal.pms.bean.model.ProductCategory;

import java.util.List;

/**
 * @author aha
 * @since 2020/11/25
 */
@Data
public class ProductCategoryTreeDTO {
    @ApiModelProperty(value = "商品分类")
    private ProductCategoryDTO productCategory;

    @ApiModelProperty(value = "下级分类")
    private List<ProductCategoryTreeDTO> children;
}
