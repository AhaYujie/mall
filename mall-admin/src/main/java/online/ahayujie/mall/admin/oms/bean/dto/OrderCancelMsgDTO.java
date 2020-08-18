package online.ahayujie.mall.admin.oms.bean.dto;

import lombok.Data;

/**
 * 取消订单消息
 * @author aha
 * @since 2020/8/14
 */
@Data
public class OrderCancelMsgDTO {
    /**
     * 订单id
     */
    private Long id;

    public OrderCancelMsgDTO() {
    }

    public OrderCancelMsgDTO(Long id) {
        this.id = id;
    }
}
