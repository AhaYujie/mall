package online.ahayujie.mall.admin.oms.bean.model;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import online.ahayujie.mall.common.bean.model.Base;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单仅退款申请
 * </p>
 *
 * @author aha
 * @since 2020-08-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("oms_order_refund_apply")
@ApiModel(value="OrderRefundApply对象", description="订单仅退款申请")
public class OrderRefundApply extends Base {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "退货商品id")
    private Long productId;

    @ApiModelProperty(value = "退货商品sku主键")
    private Long skuId;

    @ApiModelProperty(value = "会员id")
    private Long memberId;

    @ApiModelProperty(value = "仅退款原因id")
    private Long refundReasonId;

    @ApiModelProperty(value = "订单编号")
    private String orderSn;

    @ApiModelProperty(value = "会员用户名")
    private String memberUsername;

    @ApiModelProperty(value = "退款金额")
    private BigDecimal returnAmount;

    @ApiModelProperty(value = "退款原因")
    private String reasonName;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "凭证图片，以逗号隔开")
    private String proofPics;

    @ApiModelProperty(value = "处理时间")
    private Date handleTime;

    @ApiModelProperty(value = "处理备注")
    private String handleNote;

    @ApiModelProperty(value = "申请状态：0->申请中；1->已拒绝；2->处理中；3->已完成；4->已取消")
    private Integer status;
}
