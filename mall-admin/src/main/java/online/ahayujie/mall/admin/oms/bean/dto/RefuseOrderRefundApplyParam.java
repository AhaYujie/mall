package online.ahayujie.mall.admin.oms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author aha
 * @since 2020/8/27
 */
@Data
public class RefuseOrderRefundApplyParam {
    @ApiModelProperty(value = "订单仅退款申请id")
    private Long id;

    @ApiModelProperty(value = "处理备注")
    private String handleNote;
}
