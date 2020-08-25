package online.ahayujie.mall.admin.oms.bean.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author aha
 * @since 2020/8/25
 */
@Data
public class OrderDeliverMsgDTO {
    /**
     * 订单id
     */
    private Long id;

    /**
     * 物流单号
     */
    private String deliverySn;

    /**
     * 物流公司(配送方式)
     */
    private String deliveryCompany;

    /**
     * 发货时间
     */
    private Date deliveryTime;

    public OrderDeliverMsgDTO() {
    }

    public OrderDeliverMsgDTO(Long id, String deliverySn, String deliveryCompany, Date deliveryTime) {
        this.id = id;
        this.deliverySn = deliverySn;
        this.deliveryCompany = deliveryCompany;
        this.deliveryTime = deliveryTime;
    }
}
