package online.ahayujie.mall.portal.oms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author aha
 * @since 2020/12/14
 */
@Data
public class SubmitOrderProductDTO {
    @ApiModelProperty(value = "商品id")
    private Long productId;

    @ApiModelProperty(value = "商品货号")
    private String productSn;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "商品品牌名称")
    private String brandName;

    @ApiModelProperty(value = "商品分类名称")
    private String productCategoryName;

    @ApiModelProperty(value = "商品赠送积分")
    private Integer integration;

    @ApiModelProperty(value = "限制使用的积分数")
    private Integer usePointLimit;
}
