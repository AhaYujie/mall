package online.ahayujie.mall.portal.pms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author aha
 * @since 2020/10/24
 */
@Data
public class SkuDTO {
    @ApiModelProperty(value = "商品规格")
    private List<ProductDetailDTO.Specification> specifications;

    @ApiModelProperty(value = "商品sku")
    private List<ProductDetailDTO.Sku> skus;

    public SkuDTO() {
    }

    public SkuDTO(List<ProductDetailDTO.Specification> specifications, List<ProductDetailDTO.Sku> skus) {
        this.specifications = specifications;
        this.skus = skus;
    }
}
