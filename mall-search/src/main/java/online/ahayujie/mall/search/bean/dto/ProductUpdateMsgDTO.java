package online.ahayujie.mall.search.bean.dto;

import lombok.Data;

/**
 * @author aha
 * @since 2020/8/2
 */
@Data
public class ProductUpdateMsgDTO {
    private Long id;

    public ProductUpdateMsgDTO() {
    }

    public ProductUpdateMsgDTO(Long id) {
        this.id = id;
    }
}
