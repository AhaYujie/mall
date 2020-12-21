package online.ahayujie.mall.portal.oms.bean.dto;

import lombok.Data;

/**
 * @author aha
 * @since 2020/12/18
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
