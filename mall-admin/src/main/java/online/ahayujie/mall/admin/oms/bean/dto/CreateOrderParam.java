package online.ahayujie.mall.admin.oms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author aha
 * @since 2020/8/16
 */
@Data
public class CreateOrderParam {
    @ApiModelProperty(value = "会员id", required = true)
    private Long memberId;

    @ApiModelProperty(value = "购买的商品", required = true)
    private List<Product> products;

    @ApiModelProperty(value = "管理员后台调整订单使用的折扣金额，默认为0")
    private BigDecimal discountAmount;

    @Data
    public static class Product {
        @ApiModelProperty(value = "商品sku主键", required = true)
        private Long skuId;

        @ApiModelProperty(value = "购买数量", required = true)
        private Integer productQuantity;
    }
}
