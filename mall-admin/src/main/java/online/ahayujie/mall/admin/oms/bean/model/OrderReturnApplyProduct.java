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
 * 订单退货退款申请的商品
 * </p>
 *
 * @author aha
 * @since 2020-08-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("oms_order_return_apply_product")
@ApiModel(value="OrderReturnApplyProduct对象", description="订单退货退款申请的商品")
public class OrderReturnApplyProduct extends Base {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单退货退款申请id")
    private Long orderReturnApplyId;

    @ApiModelProperty(value = "订单商品id")
    private Long orderProductId;

    @ApiModelProperty(value = "商品id")
    private Long productId;

    @ApiModelProperty(value = "商品sku主键")
    private Long skuId;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "商品品牌")
    private String brandName;

    @ApiModelProperty(value = "商品分类名称")
    private String productCategoryName;

    @ApiModelProperty(value = "商品图片")
    private String productPic;

    @ApiModelProperty(value = "商品规格（json格式）")
    private String specification;

    @ApiModelProperty(value = "商品实际支付单价")
    private BigDecimal realAmount;

    @ApiModelProperty(value = "退货数量")
    private Integer returnQuantity;


}
