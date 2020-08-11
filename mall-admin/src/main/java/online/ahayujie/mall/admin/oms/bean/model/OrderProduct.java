package online.ahayujie.mall.admin.oms.bean.model;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import online.ahayujie.mall.common.bean.model.Base;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单中的商品
 * </p>
 *
 * @author aha
 * @since 2020-08-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("oms_order_product")
@ApiModel(value="OrderProduct对象", description="订单中的商品")
public class OrderProduct extends Base {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "订单编号")
    private String orderSn;

    @ApiModelProperty(value = "商品id")
    private Long productId;

    @ApiModelProperty(value = "商品sku主键")
    private Long skuId;

    @ApiModelProperty(value = "商品sku编码")
    private String productSkuCode;

    @ApiModelProperty(value = "商品货号")
    private String productSn;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "商品品牌名称")
    private String brandName;

    @ApiModelProperty(value = "商品分类名称")
    private String productCategoryName;

    @ApiModelProperty(value = "商品图片")
    private String productPic;

    @ApiModelProperty(value = "销售价格")
    private BigDecimal productPrice;

    @ApiModelProperty(value = "购买数量")
    private Integer productQuantity;

    @ApiModelProperty(value = "商品规格（json格式）")
    private String specification;

    @ApiModelProperty(value = "商品促销名称")
    private String promotionName;

    @ApiModelProperty(value = "商品促销金额")
    private BigDecimal promotionAmount;

    @ApiModelProperty(value = "优惠券优惠金额")
    private BigDecimal couponAmount;

    @ApiModelProperty(value = "积分优惠金额")
    private BigDecimal integrationAmount;

    @ApiModelProperty(value = "该商品经过优惠后的金额，即实际支付金额")
    private BigDecimal realAmount;

    @ApiModelProperty(value = "商品赠送积分")
    private Integer integration;

    @ApiModelProperty(value = "商品赠送成长值")
    private Integer growth;

    @ApiModelProperty(value = "商品状态；0->待评价；1->已评价；2->售后中；3->已退款(仅退款)；4->已退货(退货退款)")
    private Boolean status;


}
