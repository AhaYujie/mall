package online.ahayujie.mall.admin.pms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author aha
 * @since 2020/8/5
 */
@Data
public class QueryProductParam {
    @ApiModelProperty(value = "商品名称关键词")
    private String name;

    @ApiModelProperty(value = "商品货号关键词")
    private String productSn;

    @ApiModelProperty(value = "商品品牌id，0表示没有品牌")
    private Long brandId;

    @ApiModelProperty(value = "商品分类id，0表示没有分类")
    private Long productCategoryId;

    @ApiModelProperty(value = "上架状态：0->下架；1->上架")
    private Integer isPublish;

    @ApiModelProperty(value = "新品状态:0->不是新品；1->新品")
    private Integer isNew;

    @ApiModelProperty(value = "推荐状态；0->不推荐；1->推荐")
    private Integer isRecommend;

    @ApiModelProperty(value = "审核状态：0->未审核；1->审核通过")
    private Integer isVerify;

    @ApiModelProperty(value = "是否为预告商品：0->不是；1->是")
    private Integer isPreview;


}
