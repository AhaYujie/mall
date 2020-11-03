package online.ahayujie.mall.search.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author aha
 * @since 2020/11/3
 */
@Data
public class SimpleQueryProductParam {
    @ApiModelProperty(value = "关键词")
    private String keyword;

    @ApiModelProperty(value = "sort：0->按相关度排序；1->按销量从高到低排序；2->按价格从高到低排序；3->按价格从低到高排序；默认0")
    private Integer sort;

    @ApiModelProperty(value = "页索引，默认1")
    private Integer pageNum;

    @ApiModelProperty(value = "页大小，默认20")
    private Integer pageSize;
}
