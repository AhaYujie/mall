package online.ahayujie.mall.portal.pms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author aha
 * @since 2020/11/6
 */
@Data
public class QueryEsProductParam {
    @ApiModelProperty(value = "关键词")
    private String keyword;

    @ApiModelProperty(value = "商品品牌id，0表示没有品牌")
    private Long brandId;

    @ApiModelProperty(value = "商品分类id，0表示没有分类")
    private Long productCategoryId;

    @ApiModelProperty(value = "最低价格")
    private BigDecimal minPrice;

    @ApiModelProperty(value = "最高价格")
    private BigDecimal maxPrice;

    @ApiModelProperty(value = "上架状态：0->下架；1->上架")
    private Integer isPublish;

    @ApiModelProperty(value = "sort：0->按相关度排序；1->按销量从高到低排序；2->按价格从高到低排序；3->按价格从低到高排序；默认0")
    private Integer sort;

    @ApiModelProperty(value = "页索引，默认1")
    private Integer pageNum;

    @ApiModelProperty(value = "页大小，默认20")
    private Integer pageSize;
}
