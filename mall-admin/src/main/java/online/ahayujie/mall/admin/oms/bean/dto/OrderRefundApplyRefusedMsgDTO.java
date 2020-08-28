package online.ahayujie.mall.admin.oms.bean.dto;

import lombok.Data;

/**
 * @author aha
 * @since 2020/8/28
 */
@Data
public class OrderRefundApplyRefusedMsgDTO {
    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 订单仅退款申请id
     */
    private Long orderRefundApplyId;

    /**
     * 处理备注
     */
    private String handleNote;

    public OrderRefundApplyRefusedMsgDTO() {
    }

    public OrderRefundApplyRefusedMsgDTO(Long orderId, Long orderRefundApplyId, String handleNote) {
        this.orderId = orderId;
        this.orderRefundApplyId = orderRefundApplyId;
        this.handleNote = handleNote;
    }
}
