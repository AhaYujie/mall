package online.ahayujie.mall.portal.oms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author aha
 * @since 2020/11/7
 */
@Data
public class GenerateConfirmOrderParam {
    @ApiModelProperty(value = "购买的商品")
    private List<Product> products;

    @ApiModelProperty(value = "会员收货地址id")
    private Long receiveAddressId;

    @ApiModelProperty(value = "使用的积分")
    private Integer usedIntegration;

    @Data
    public static class Product {
        @ApiModelProperty(value = "sku主键")
        private Long skuId;

        @ApiModelProperty(value = "购买数量")
        private Integer quantity;
    }
}
