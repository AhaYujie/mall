package online.ahayujie.mall.admin.pms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author aha
 * @since 2020/7/22
 */
@Data
public class UpdateSkuParam {
    @ApiModelProperty(value = "sku列表", notes = "新增的sku不传id，更新的sku传id，不允许删除sku")
    private List<UpdateSku> skus;

    @Data
    public static class UpdateSku {
        @ApiModelProperty(value = "sku主键")
        private Long id;

        @ApiModelProperty(value = "sku编码，若是新增的sku的skuCode且skuCode为空则自动生成skuCode")
        private String skuCode;

        @ApiModelProperty(value = "价格")
        private BigDecimal price;

        @ApiModelProperty(value = "库存")
        private Integer stock;

        @ApiModelProperty(value = "预警库存")
        private Integer lowStock;

        @ApiModelProperty(value = "默认展示图片url")
        private String pic;

        @ApiModelProperty(value = "图片", notes = "图片列表为null则不做处理，不为null则用新图片替换旧图片")
        private List<UpdateSkuImage> images;

        @ApiModelProperty(value = "sku和商品规格关系", notes = "新增sku才需要传")
        private List<UpdateSkuSpecificationRelationship> specifications;
    }

    @Data
    public static class UpdateSkuImage {
        @ApiModelProperty(value = "图片url")
        private String image;
    }

    @Data
    public static class UpdateSkuSpecificationRelationship {
        @ApiModelProperty(value = "规格id")
        private Long specificationId;

        @ApiModelProperty(value = "规格选项id")
        private Long specificationValueId;
    }
}
