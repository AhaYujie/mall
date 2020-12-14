package online.ahayujie.mall.portal.oms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author aha
 * @since 2020/11/7
 */
@Data
public class ConfirmOrderDTO {
    @ApiModelProperty(value = "购买的商品")
    private List<Product> products;

    @ApiModelProperty(value = "会员的收货地址")
    private ReceiveAddress receiveAddress;

    @ApiModelProperty(value = "积分信息")
    private IntegrationInfo integrationInfo;

    @ApiModelProperty(value = "金额")
    private Amount amount;

    @Data
    public static class Product {
        @ApiModelProperty(value = "商品id")
        private Long id;

        @ApiModelProperty(value = "sku主键")
        private Long skuId;

        @ApiModelProperty(value = "商品名称")
        private String name;

        @ApiModelProperty(value = "默认展示图片url")
        private String pic;

        @ApiModelProperty(value = "商品规格，json格式")
        private String specification;

        @ApiModelProperty(value = "计量单位，最大长度为5")
        private String unit;

        @ApiModelProperty(value = "赠送的积分")
        private Integer giftPoint;

        @ApiModelProperty(value = "单个商品限制使用的积分数")
        private Integer usePointLimit;

        @ApiModelProperty(value = "价格")
        private BigDecimal price;

        @ApiModelProperty(value = "购买数量")
        private Integer quantity;
    }

    @Data
    public static class ReceiveAddress {
        @ApiModelProperty(value = "收货地址id")
        private Long id;

        @ApiModelProperty(value = "收货人名称")
        private String name;

        @ApiModelProperty(value = "收货人手机号")
        private String phoneNumber;

        @ApiModelProperty(value = "省份/直辖市")
        private String province;

        @ApiModelProperty(value = "城市")
        private String city;

        @ApiModelProperty(value = "区")
        private String region;

        @ApiModelProperty(value = "街道")
        private String street;

        @ApiModelProperty(value = "详细地址(街道)")
        private String detailAddress;

        @ApiModelProperty(value = "是否为默认，0->不是，1->是")
        private Integer isDefault;
    }

    @Data
    public static class IntegrationInfo {
        @ApiModelProperty(value = "会员拥有的积分")
        private Integer integration;

        @ApiModelProperty(value = "最多可以抵扣的积分")
        private Integer maxUseIntegration;

        @ApiModelProperty(value = "使用的积分")
        private Integer usedIntegration;

        @ApiModelProperty(value = "积分和金额的比例，即多少积分可以抵扣一元")
        private BigDecimal integrationRatio;

        @ApiModelProperty(value = "使用积分的最小单位，即使用的积分需要是最小单位的倍数")
        private Integer integrationUseUnit;
    }

    @Data
    public static class Amount {
        @ApiModelProperty(value = "使用积分抵扣的金额")
        private BigDecimal integrationAmount;

        @ApiModelProperty(value = "商品合计金额")
        private BigDecimal productAmount;

        @ApiModelProperty(value = "运费金额")
        private BigDecimal freightAmount;

        @ApiModelProperty(value = "应付金额（实际支付金额）")
        private BigDecimal payAmount;
    }
}
