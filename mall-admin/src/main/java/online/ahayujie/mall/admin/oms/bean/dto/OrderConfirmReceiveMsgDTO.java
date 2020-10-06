package online.ahayujie.mall.admin.oms.bean.dto;

import lombok.Data;

/**
 * @author aha
 * @since 2020/10/3
 */
@Data
public class OrderConfirmReceiveMsgDTO {
    /**
     * 订单id
     */
    private Long orderId;

    public OrderConfirmReceiveMsgDTO() {
    }

    public OrderConfirmReceiveMsgDTO(Long orderId) {
        this.orderId = orderId;
    }
}
