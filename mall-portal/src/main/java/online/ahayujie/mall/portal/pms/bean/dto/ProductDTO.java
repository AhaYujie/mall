package online.ahayujie.mall.portal.pms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author aha
 * @since 2020/11/6
 */
@Data
public class ProductDTO {
    @ApiModelProperty(value = "id")
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

    @ApiModelProperty(value = "图片url，用于列表显示")
    private String pic;

    @ApiModelProperty(value = "副标题，用于列表显示")
    private String subTitle;

    @ApiModelProperty(value = "商品简要描述，用于列表显示")
    private String description;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "市场价")
    private BigDecimal originalPrice;

    @ApiModelProperty(value = "销量")
    private Integer sale;

    @ApiModelProperty(value = "库存")
    private Integer stock;

    @ApiModelProperty(value = "计量单位，最大长度为5")
    private String unit;

    @ApiModelProperty(value = "商品关键词")
    private String keywords;

    @ApiModelProperty(value = "以逗号分割的产品服务：1->无忧退货；2->快速退款；3->免费包邮")
    private String serviceIds;

    @ApiModelProperty(value = "促销类型：0->没有促销使用原价;1->使用促销价；2->使用会员价；3->使用阶梯价格；4->使用满减价格；5->限时购，默认为0")
    private Integer promotionType;

    @ApiModelProperty(value = "新品状态:0->不是新品；1->新品，默认为0")
    private Integer isNew;

    @ApiModelProperty(value = "是否为预告商品：0->不是；1->是，默认为0")
    private Integer isPreview;
}
