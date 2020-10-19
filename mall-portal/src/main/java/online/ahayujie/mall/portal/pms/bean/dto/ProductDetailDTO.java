package online.ahayujie.mall.portal.pms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author aha
 * @since 2020/10/17
 */
@Data
public class ProductDetailDTO {
    @ApiModelProperty(value = "商品信息")
    private ProductInfo productInfo;

    @ApiModelProperty(value = "商品图片")
    private List<String> productImages;

    @ApiModelProperty(value = "商品参数")
    private List<Param> params;

    @ApiModelProperty(value = "商品规格")
    private List<Specification> specifications;

    @ApiModelProperty(value = "商品sku")
    private List<Sku> skus;

    public ProductDetailDTO() {
    }

    public ProductDetailDTO(ProductInfo productInfo, List<String> productImages, List<Param> params,
                            List<Specification> specifications, List<Sku> skus) {
        this.productInfo = productInfo;
        this.productImages = productImages;
        this.params = params;
        this.specifications = specifications;
        this.skus = skus;
    }

    @Data
    public static class ProductInfo {
        @ApiModelProperty(value = "商品id")
        private Long id;

        @ApiModelProperty(value = "商品品牌id，0表示没有品牌")
        private Long brandId;

        @ApiModelProperty(value = "商品分类id，0表示没有分类")
        private Long productCategoryId;

        @ApiModelProperty(value = "商品名称")
        private String name;

        @ApiModelProperty(value = "品牌名称")
        private String brandName;

        @ApiModelProperty(value = "商品分类名称")
        private String productCategoryName;

        @ApiModelProperty(value = "详细页标题")
        private String detailTitle;

        @ApiModelProperty(value = "详细页描述")
        private String detailDescription;

        @ApiModelProperty(value = "详情网页内容，detailMobileHtml不为null则这个为null")
        private String detailHtml;

        @ApiModelProperty(value = "移动端网页详情，detailHtml不为null则这个为null")
        private String detailMobileHtml;

        @ApiModelProperty(value = "价格")
        private BigDecimal price;

        @ApiModelProperty(value = "市场价")
        private BigDecimal originalPrice;

        @ApiModelProperty(value = "计量单位，最大长度为5")
        private String unit;

        @ApiModelProperty(value = "赠送的积分")
        private Integer giftPoint;

        @ApiModelProperty(value = "限制使用的积分数")
        private Integer usePointLimit;

        @ApiModelProperty(value = "以逗号分割的产品服务：1->无忧退货；2->快速退款；3->免费包邮")
        private String serviceIds;

        @ApiModelProperty(value = "促销类型：0->没有促销使用原价;1->使用促销价；2->使用会员价；3->使用阶梯价格；4->使用满减价格；5->限时购，默认为0")
        private Integer promotionType;

        @ApiModelProperty(value = "新品状态:0->不是新品；1->新品，默认为0")
        private Boolean isNew;
    }

    @Data
    public static class Param {
        @ApiModelProperty(value = "参数名称")
        private String name;

        @ApiModelProperty(value = "参数值")
        private String value;

        @ApiModelProperty(value = "参数类型：0->纯文本，1->图文")
        private Integer type;
    }

    @Data
    public static class Specification {
        @ApiModelProperty(value = "商品规格id")
        private Long id;

        @ApiModelProperty(value = "规格名称")
        private String name;

        @ApiModelProperty(value = "规格值列表")
        private List<SpecificationValue> values;
    }

    @Data
    public static class SpecificationValue {
        @ApiModelProperty(value = "商品规格选项id")
        private Long id;

        @ApiModelProperty(value = "选项值")
        private String value;

        @ApiModelProperty(value = "选项类型：0->纯文本，1->图文")
        private Integer type;
    }

    @Data
    public static class Sku {
        @ApiModelProperty(value = "sku主键")
        private Long id;

        @ApiModelProperty(value = "价格")
        private BigDecimal price;

        @ApiModelProperty(value = "单品促销价格")
        private BigDecimal promotionPrice;

        @ApiModelProperty(value = "库存")
        private Integer stock;

        @ApiModelProperty(value = "默认展示图片url")
        private String pic;

        @ApiModelProperty(value = "商品规格，json格式")
        private String specification;

        @ApiModelProperty(value = "sku和商品规格关系")
        private List<SkuSpecificationRelationship> relationships;
    }

    @Data
    public static class SkuSpecificationRelationship {
        @ApiModelProperty(value = "商品规格id")
        private Long specificationId;

        @ApiModelProperty(value = "商品规格选项id")
        private Long specificationValueId;
    }
}
