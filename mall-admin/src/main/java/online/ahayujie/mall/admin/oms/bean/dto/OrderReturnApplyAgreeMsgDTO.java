package online.ahayujie.mall.admin.oms.bean.dto;

import lombok.Data;

/**
 * @author aha
 * @since 2020/8/29
 */
@Data
public class OrderReturnApplyAgreeMsgDTO {
    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 订单仅退款申请id
     */
    private Long orderRefundApplyId;

    public OrderReturnApplyAgreeMsgDTO() {
    }

    public OrderReturnApplyAgreeMsgDTO(Long orderId, Long orderRefundApplyId) {
        this.orderId = orderId;
        this.orderRefundApplyId = orderRefundApplyId;
    }
}
