package online.ahayujie.mall.admin.pms.bean.dto;

import lombok.Data;

/**
 * @author aha
 * @since 2020/7/28
 */
@Data
public class UpdateProductCategoryMessageDTO {
    /**
     * 商品分类id
     */
    private Long id;

    public UpdateProductCategoryMessageDTO() {
    }

    public UpdateProductCategoryMessageDTO(Long id) {
        this.id = id;
    }
}
