package online.ahayujie.mall.admin.oms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author aha
 * @since 2020/8/12
 */
@Data
public class UpdateOrderReturnReasonParam {
    @ApiModelProperty(value = "退货原因名称")
    private String name;

    @ApiModelProperty(value = "排序，从大到小")
    private Integer sort;

    @ApiModelProperty(value = "状态：0->不启用；1->启用")
    private Integer status;
}
