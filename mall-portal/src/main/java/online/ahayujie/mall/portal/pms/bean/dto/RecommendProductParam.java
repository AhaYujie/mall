package online.ahayujie.mall.portal.pms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author aha
 * @since 2020/11/6
 */
@Data
public class RecommendProductParam {
    @ApiModelProperty(value = "商品id")
    private Long id;

    @ApiModelProperty(value = "页索引，默认1")
    private Integer pageNum;

    @ApiModelProperty(value = "页大小，默认5")
    private Integer pageSize;
}
