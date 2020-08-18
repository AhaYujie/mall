package online.ahayujie.mall.admin.oms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author aha
 * @since 2020/8/13
 */
@Data
public class QueryOrderListParam {
    @ApiModelProperty(value = "订单编号")
    private String orderSn;

    @ApiModelProperty(value = "订单状态：0->待付款；1->待发货；2->已发货；3->待评价；4->交易完成；5->申请仅退款；6->申请退货退款；7->仅退款中；8->退货退款中；9->交易关闭")
    private Integer status;

    @ApiModelProperty(value = "订单类型：0->正常订单；1->秒杀订单")
    private Integer orderType;

    @ApiModelProperty(value = "订单来源：0->PC订单；1->APP订单")
    private Integer sourceType;

    @ApiModelProperty(value = "会员用户名")
    private String memberUsername;
}
