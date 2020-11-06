package online.ahayujie.mall.portal.pms.bean.dto;

import lombok.Data;
import online.ahayujie.mall.common.api.CommonPage;

/**
 * @author aha
 * @since 2020/11/6
 */
@Data
public class RecommendEsProductDTO {
    private Integer code;

    private String message;

    private CommonPage<EsProduct> data;
}
