package online.ahayujie.mall.admin.pms.bean.dto;

import lombok.Data;

/**
 * @author aha
 * @since 2020/7/30
 */
@Data
public class UpdateBrandMessageDTO {
    /**
     * 品牌id
     */
    private Long id;

    public UpdateBrandMessageDTO() {
    }

    public UpdateBrandMessageDTO(Long id) {
        this.id = id;
    }
}
