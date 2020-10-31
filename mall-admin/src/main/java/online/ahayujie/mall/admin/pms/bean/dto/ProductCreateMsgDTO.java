package online.ahayujie.mall.admin.pms.bean.dto;

import lombok.Data;

/**
 * @author aha
 * @since 2020/10/31
 */
@Data
public class ProductCreateMsgDTO {
    /**
     * 商品id
     */
    private Long id;

    public ProductCreateMsgDTO() {
    }

    public ProductCreateMsgDTO(Long id) {
        this.id = id;
    }
}
