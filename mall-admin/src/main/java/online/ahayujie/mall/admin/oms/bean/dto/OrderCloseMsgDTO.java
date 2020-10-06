package online.ahayujie.mall.admin.oms.bean.dto;

import lombok.Data;

/**
 * @author aha
 * @since 2020/10/4
 */
@Data
public class OrderCloseMsgDTO {
    /**
     * 订单id
     */
    private Long id;

    public OrderCloseMsgDTO() {
    }

    public OrderCloseMsgDTO(Long id) {
        this.id = id;
    }
}
