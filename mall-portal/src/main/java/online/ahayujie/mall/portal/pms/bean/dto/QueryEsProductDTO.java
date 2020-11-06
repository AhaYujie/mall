package online.ahayujie.mall.portal.pms.bean.dto;

import lombok.Data;
import online.ahayujie.mall.common.api.CommonPage;

/**
 * @author aha
 * @since 2020/11/3
 */
@Data
public class QueryEsProductDTO {
    private Integer code;

    private String message;

    private CommonPage<EsProduct> data;
}
