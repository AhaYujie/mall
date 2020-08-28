package online.ahayujie.mall.admin.oms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author aha
 * @since 2020/8/28
 */
@Data
public class RefuseOrderReturnApplyParam {
    @ApiModelProperty(value = "订单退货退款申请id")
    private Long id;

    @ApiModelProperty(value = "订单退货退款申请id")
    private String handleNote;
}
