package online.ahayujie.mall.admin.oms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import online.ahayujie.mall.admin.oms.bean.model.OrderRefundApply;
import online.ahayujie.mall.admin.oms.bean.model.OrderRefundApplyProduct;

import java.util.List;

/**
 * @author aha
 * @since 2020/8/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderRefundApplyDetailDTO extends OrderRefundApply {
    @ApiModelProperty(value = "仅退款商品")
    private List<OrderRefundApplyProduct> products;
}
