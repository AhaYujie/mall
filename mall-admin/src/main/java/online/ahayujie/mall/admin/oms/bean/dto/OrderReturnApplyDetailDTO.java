package online.ahayujie.mall.admin.oms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import online.ahayujie.mall.admin.oms.bean.model.OrderReturnApply;
import online.ahayujie.mall.admin.oms.bean.model.OrderReturnApplyProduct;

import java.util.List;

/**
 * @author aha
 * @since 2020/8/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderReturnApplyDetailDTO extends OrderReturnApply {
    @ApiModelProperty(value = "退货退款申请的商品")
    private List<OrderReturnApplyProduct> products;
}
