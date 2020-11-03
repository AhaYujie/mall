package online.ahayujie.mall.search.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author aha
 * @since 2020/11/3
 */
@Data
public class QueryProductParam {
    @ApiModelProperty(value = "关键词")
    private String keyword;

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

    @ApiModelProperty(value = "最低价格")
    private BigDecimal minPrice;

    @ApiModelProperty(value = "最高价格")
    private BigDecimal maxPrice;

    @ApiModelProperty(value = "sort：0->按相关度排序；1->按销量从高到低排序；2->按价格从高到低排序；3->按价格从低到高排序；默认0")
    private Integer sort;

    @ApiModelProperty(value = "页索引，默认1")
    private Integer pageNum;

    @ApiModelProperty(value = "页大小，默认20")
    private Integer pageSize;
}
