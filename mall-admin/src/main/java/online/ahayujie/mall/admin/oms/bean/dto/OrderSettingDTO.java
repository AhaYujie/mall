package online.ahayujie.mall.admin.oms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author aha
 * @since 2020/8/13
 */
@Data
public class OrderSettingDTO {
    @ApiModelProperty(value = "订单未支付超时关闭的时间，单位分钟")
    private Integer unPayTimeOut;

    @ApiModelProperty(value = "发货未确认收货超时自动确认时间(单位天)")
    private Integer autoConfirmReceiveTime;

    @ApiModelProperty(value = "确认收货后未评价超时自动评价时间(单位天)")
    private Integer autoCommentTime;

    @ApiModelProperty(value = "订单交易完成后自动关闭交易，不能申请售后的时间(单位天)")
    private Integer autoCloseTime;
}
