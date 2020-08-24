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
 * 订单表
 * </p>
 *
 * @author aha
 * @since 2020-08-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("oms_order")
@ApiModel(value="Order对象", description="订单表")
public class Order extends Base {
    // 订单状态实现类的beanId
    public static final String UN_PAY_STATUS_NAME = "unPayOrderState";
    public static final String UN_DELIVER_STATUS_NAME = "unDeliverOrderState";
    public static final String DELIVERED_STATUS_NAME = "deliveredOrderState";
    public static final String UN_COMMENT_STATUS_NAME = "unCommentOrderState";
    public static final String COMPLETE_STATUS_NAME = "completeOrderState";
    public static final String APPLY_REFUND_STATUS_NAME = "applyRefundOrderState";
    public static final String APPLY_RETURN_STATUS_NAME = "applyReturnOrderState";
    public static final String REFUND_STATUS_NAME = "refundOrderState";
    public static final String RETURN_STATUS_NAME = "returnOrderState";
    public static final String CLOSED_STATUS_NAME = "closedOrderState";

    /**
     * 订单状态
     */
    public enum Status {
        /**
         * 待付款状态
         */
        UN_PAY(0, UN_PAY_STATUS_NAME),

        /**
         * 待发货状态
         */
        UN_DELIVER(1, UN_DELIVER_STATUS_NAME),

        /**
         * 已发货状态
         */
        DELIVERED(2, DELIVERED_STATUS_NAME),

        /**
         * 待评价状态
         */
        UN_COMMENT(3, UN_COMMENT_STATUS_NAME),

        /**
         * 交易完成状态
         */
        COMPLETE(4, COMPLETE_STATUS_NAME),

        /**
         * 申请仅退款状态
         */
        APPLY_REFUND(5, APPLY_REFUND_STATUS_NAME),

        /**
         * 申请退款退货状态
         */
        APPLY_RETURN(6, APPLY_RETURN_STATUS_NAME),

        /**
         * 仅退款中状态
         */
        REFUND(7, REFUND_STATUS_NAME),

        /**
         * 退货退款中状态
         */
        RETURN(8, RETURN_STATUS_NAME),

        /**
         * 交易关闭状态
         */
        CLOSED(9, CLOSED_STATUS_NAME)
        ;
        /**
         * 订单状态值
         */
        private final Integer value;

        /**
         * 订单状态实现类beanId
         */
        private final String name;

        Status(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 订单类型
     */
    public enum Type {
        /**
         * 正常订单
         */
        NORMAL(0),

        /**
         * 秒杀订单
         */
        MIAO_SHA(1)
        ;
        private final Integer value;

        Type(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    /**
     * 订单来源类型
     */
    public enum SourceType {
        /**
         * PC订单
         */
        PC(0),

        /**
         * APP订单
         */
        APP(1)
        ;
        private final Integer value;

        SourceType(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    /**
     * 订单支付方式
     */
    public enum PayType {
        /**
         * 未支付
         */
        UN_PAY(0),

        /**
         * 支付宝
         */
        AliPay(1),

        /**
         * 微信支付
         */
        WeChatPay(2)
        ;
        private final Integer value;

        PayType(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "支付时间")
    private Date payTime;

    @ApiModelProperty(value = "确认收货时间")
    private Date receiveTime;

    @ApiModelProperty(value = "会员id")
    private Long memberId;

    @ApiModelProperty(value = "会员用户名")
    private String memberUsername;

    @ApiModelProperty(value = "订单编号")
    private String orderSn;

    @ApiModelProperty(value = "订单状态：0->待付款；1->待发货；2->已发货；3->待评价；4->交易完成；5->申请仅退款；6->申请退货退款；7->仅退款中；8->退货退款中；9->交易关闭")
    private Integer status;

    @ApiModelProperty(value = "订单类型：0->正常订单；1->秒杀订单")
    private Integer orderType;

    @ApiModelProperty(value = "订单来源：0->PC订单；1->APP订单")
    private Integer sourceType;

    @ApiModelProperty(value = "应付金额（实际支付金额）")
    private BigDecimal payAmount;

    @ApiModelProperty(value = "订单总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "运费金额")
    private BigDecimal freightAmount;

    @ApiModelProperty(value = "促销金额（促销价、满减、阶梯价）")
    private BigDecimal promotionAmount;

    @ApiModelProperty(value = "积分抵扣金额")
    private BigDecimal integrationAmount;

    @ApiModelProperty(value = "优惠券抵扣金额")
    private BigDecimal couponAmount;

    @ApiModelProperty(value = "管理员后台调整订单使用的折扣金额")
    private BigDecimal discountAmount;

    @ApiModelProperty(value = "支付方式：0->未支付；1->支付宝；2->微信")
    private Integer payType;

    @ApiModelProperty(value = "物流公司(配送方式)")
    private String deliveryCompany;

    @ApiModelProperty(value = "物流单号")
    private String deliverySn;

    @ApiModelProperty(value = "发货时间")
    private Date deliveryTime;

    @ApiModelProperty(value = "收货人姓名")
    private String receiverName;

    @ApiModelProperty(value = "收货人电话")
    private String receiverPhone;

    @ApiModelProperty(value = "省份/直辖市")
    private String receiverProvince;

    @ApiModelProperty(value = "城市")
    private String receiverCity;

    @ApiModelProperty(value = "区")
    private String receiverRegion;

    @ApiModelProperty(value = "街道")
    private String receiverStreet;

    @ApiModelProperty(value = "详细地址")
    private String receiverDetailAddress;

    @ApiModelProperty(value = "优惠券id，逗号隔开")
    private String couponIds;

    @ApiModelProperty(value = "下单时使用的积分")
    private Integer useIntegration;

    @ApiModelProperty(value = "获得的积分")
    private Integer integration;

    @ApiModelProperty(value = "活动信息")
    private String promotionInfo;

    @ApiModelProperty(value = "订单备注")
    private String note;


}
