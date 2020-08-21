package online.ahayujie.mall.admin.pms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author aha
 * @since 2020/7/14
 */
@Data
public class CreateProductParam {
    @ApiModelProperty(value = "商品信息", required = true)
    private CreateProduct product;

    @ApiModelProperty(value = "商品规格")
    private List<CreateSpecification> specifications;

    @ApiModelProperty(value = "sku")
    private List<CreateSku> skus;

    @ApiModelProperty(value = "商品参数")
    private List<CreateParam> params;

    @Data
    public static class CreateProduct {
        @ApiModelProperty(value = "商品品牌id，0表示没有品牌，默认为0")
        private Long brandId;

        @ApiModelProperty(value = "商品分类id，0表示没有分类，默认为0")
        private Long productCategoryId;

        @ApiModelProperty(value = "货号")
        private String productSn;

        @ApiModelProperty(value = "商品名称", required = true)
        private String name;

        @ApiModelProperty(value = "图片url，用于列表显示")
        private String pic;

        @ApiModelProperty(value = "副标题，用于列表显示")
        private String subTitle;

        @ApiModelProperty(value = "商品简要描述，用于列表显示")
        private String description;

        @ApiModelProperty(value = "详细页标题")
        private String detailTitle;

        @ApiModelProperty(value = "详细页描述")
        private String detailDescription;

        @ApiModelProperty(value = "详情网页内容")
        private String detailHtml;

        @ApiModelProperty(value = "移动端网页详情")
        private String detailMobileHtml;

        @ApiModelProperty(value = "价格")
        private BigDecimal price;

        @ApiModelProperty(value = "市场价")
        private BigDecimal originalPrice;

        @ApiModelProperty(value = "库存")
        private Integer stock;

        @ApiModelProperty(value = "库存预警值")
        private Integer lowStock;

        @ApiModelProperty(value = "计量单位，最大长度为5")
        private String unit;

        @ApiModelProperty(value = "商品备注")
        private String note;

        @ApiModelProperty(value = "商品关键词")
        private String keywords;

        @ApiModelProperty(value = "排序，从大到小")
        private Integer sort;

        @ApiModelProperty(value = "赠送的积分")
        private Integer giftPoint;

        @ApiModelProperty(value = "限制使用的积分数")
        private Integer usePointLimit;

        @ApiModelProperty(value = "以逗号分割的产品服务：1->无忧退货；2->快速退款；3->免费包邮")
        private String serviceIds;

        @ApiModelProperty(value = "促销类型：0->没有促销使用原价;1->使用促销价；2->使用会员价；3->使用阶梯价格；4->使用满减价格；5->限时购，默认为0")
        private Integer promotionType;

        @ApiModelProperty(value = "上架状态：0->下架；1->上架，默认为0")
        private Integer isPublish;

        @ApiModelProperty(value = "新品状态:0->不是新品；1->新品，默认为0")
        private Integer isNew;

        @ApiModelProperty(value = "推荐状态；0->不推荐；1->推荐，默认为0")
        private Integer isRecommend;

        @ApiModelProperty(value = "是否为预告商品：0->不是；1->是，默认为0")
        private Integer isPreview;

        @ApiModelProperty(value = "商品图片")
        private List<CreateProductImage> images;
    }

    @Data
    public static class CreateProductImage {
        @ApiModelProperty(value = "图片url")
        private String image;
    }

    @Data
    public static class CreateSpecification {
        @ApiModelProperty(value = "规格名称")
        private String name;

        @ApiModelProperty(value = "规格选项")
        private List<CreateSpecificationValue> values;
    }

    @Data
    public static class CreateSpecificationValue {
        @ApiModelProperty(value = "选项值")
        private String value;

        @ApiModelProperty(value = "选项类型：0->纯文本，1->图文")
        private Integer type;
    }

    @Data
    public static class CreateSku {
        @ApiModelProperty(value = "sku编码，空则自动生成")
        private String skuCode;

        @ApiModelProperty(value = "价格")
        private BigDecimal price;

        @ApiModelProperty(value = "库存")
        private Integer stock;

        @ApiModelProperty(value = "预警库存")
        private Integer lowStock;

        @ApiModelProperty(value = "默认展示图片url")
        private String pic;

        @ApiModelProperty(value = "图片")
        private List<CreateSkuImage> images;

        @ApiModelProperty(value = "sku对于的商品规格索引，第1个值对应规格列表第1个规格的规格值列表索引，" +
                "第2个值对应规格列表第2个规格的规格值列表索引，以此类推")
        private List<Integer> specificationIndex;
    }

    @Data
    public static class CreateSkuImage {
        @ApiModelProperty(value = "图片url")
        private String image;
    }

    @Data
    public static class CreateParam {
        @ApiModelProperty(value = "参数名称")
        private String name;

        @ApiModelProperty(value = "参数值")
        private String value;

        @ApiModelProperty(value = "参数类型：0->纯文本，1->图文")
        private Integer type;
    }
}
