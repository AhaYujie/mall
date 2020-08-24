package online.ahayujie.mall.admin.oms.bean.dto;

import lombok.Data;

/**
 * @author aha
 * @since 2020/8/24
 */
@Data
public class OrderCancelledMsgDTO {
    /**
     * 订单id
     */
    private Long id;

    public OrderCancelledMsgDTO() {
    }

    public OrderCancelledMsgDTO(Long id) {
        this.id = id;
    }
}
