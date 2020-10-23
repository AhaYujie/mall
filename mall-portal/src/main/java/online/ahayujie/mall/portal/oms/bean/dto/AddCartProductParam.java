package online.ahayujie.mall.portal.oms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author aha
 * @since 2020/10/23
 */
@Data
public class AddCartProductParam {
    @ApiModelProperty(value = "商品的id")
    private Long productId;

    @ApiModelProperty(value = "商品sku的id")
    private Long skuId;

    @ApiModelProperty(value = "购买数量")
    private Integer quantity;
}
