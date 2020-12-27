package online.ahayujie.mall.admin.oms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author aha
 * @since 2020/12/27
 */
@Data
public class OrderTimedJobStatusDTO {
    @ApiModelProperty(value = "订单定时任务状态; -1->不存在; 0->正常; 1->暂停")
    private Integer status;
}
