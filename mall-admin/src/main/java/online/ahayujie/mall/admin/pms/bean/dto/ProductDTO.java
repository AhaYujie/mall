package online.ahayujie.mall.admin.pms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import online.ahayujie.mall.admin.pms.bean.model.*;

import java.util.List;

/**
 * @author aha
 * @since 2020/7/19
 */
@Data
public class ProductDTO {
    @ApiModelProperty(value = "商品信息")
    private Product product;

    @ApiModelProperty(value = "商品图片")
    private List<ProductImage> productImages;

    @ApiModelProperty(value = "商品参数")
    private List<ProductParam> productParams;

    @ApiModelProperty(value = "商品规格列表")
    private List<SpecificationDTO> specifications;

    @ApiModelProperty(value = "商品sku列表")
    private List<SkuDTO> skus;

    @Data
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class SpecificationDTO extends ProductSpecification {
        @ApiModelProperty(value = "商品规格选项")
        private List<ProductSpecificationValue> specificationValues;
    }

    @Data
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class SkuDTO extends Sku {
        @ApiModelProperty(value = "商品sku图片")
        private List<SkuImage> skuImages;
    }
}
