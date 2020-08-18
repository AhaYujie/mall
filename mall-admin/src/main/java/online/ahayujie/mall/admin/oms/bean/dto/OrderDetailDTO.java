package online.ahayujie.mall.admin.oms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import online.ahayujie.mall.admin.oms.bean.model.Order;
import online.ahayujie.mall.admin.oms.bean.model.OrderProduct;

import java.util.List;

/**
 * @author aha
 * @since 2020/8/14
 */
@Data
public class OrderDetailDTO {
    @ApiModelProperty(value = "订单信息")
    private Order order;

    @ApiModelProperty(value = "订单商品信息")
    private List<OrderProduct> orderProducts;

    public OrderDetailDTO() {
    }

    public OrderDetailDTO(Order order, List<OrderProduct> orderProducts) {
        this.order = order;
        this.orderProducts = orderProducts;
    }
}
