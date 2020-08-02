package online.ahayujie.mall.admin.pms.bean.dto;

import lombok.Data;

/**
 * @author aha
 * @since 2020/8/2
 */
@Data
public class UpdateProductMessageDTO {
    private Long id;

    public UpdateProductMessageDTO() {
    }

    public UpdateProductMessageDTO(Long id) {
        this.id = id;
    }
}
