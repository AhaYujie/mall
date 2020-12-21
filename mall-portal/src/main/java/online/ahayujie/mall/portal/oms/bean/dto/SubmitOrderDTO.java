package online.ahayujie.mall.portal.oms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author aha
 * @since 2020/12/14
 */
@Data
public class SubmitOrderDTO {
    @ApiModelProperty(value = "订单id")
    private Long id;

    @ApiModelProperty(value = "订单编号")
    private String orderSn;

    @ApiModelProperty(value = "支付金额")
    private BigDecimal payAmount;
}
