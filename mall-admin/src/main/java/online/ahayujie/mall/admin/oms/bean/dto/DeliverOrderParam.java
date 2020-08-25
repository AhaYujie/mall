package online.ahayujie.mall.admin.oms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author aha
 * @since 2020/8/25
 */
@Data
public class DeliverOrderParam {
    @ApiModelProperty(value = "订单id")
    private Long id;

    @ApiModelProperty(value = "物流单号")
    private String deliverySn;

    @ApiModelProperty(value = "物流公司(配送方式)")
    private String deliveryCompany;

    @ApiModelProperty(value = "发货时间")
    private Date deliveryTime;
}
