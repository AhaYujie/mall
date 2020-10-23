package online.ahayujie.mall.portal.oms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author aha
 * @since 2020/10/22
 */
@Data
public class CartProductDTO {
    public static final int NOT_PUBLISH = 0;
    public static final int PUBLISH = 1;

    public static final int SKU_NOT_EXIST = 0;
    public static final int SKU_EXIST = 1;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "商品的id")
    private Long productId;

    @ApiModelProperty(value = "商品sku的id")
    private Long skuId;

    @ApiModelProperty(value = "商品品牌id")
    private Long brandId;

    @ApiModelProperty(value = "商品分类id")
    private Long productCategoryId;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "商品品牌名称")
    private String brandName;

    @ApiModelProperty(value = "商品分类名称")
    private String productCategoryName;

    @ApiModelProperty(value = "商品图片")
    private String skuPic;

    @ApiModelProperty(value = "商品规格（json格式）")
    private String specification;

    @ApiModelProperty(value = "购买数量")
    private Integer quantity;

    @ApiModelProperty(value = "单件商品价格")
    private BigDecimal price;

    @ApiModelProperty(value = "是否上架，0->下架，1->上架")
    private Integer isPublish;

    @ApiModelProperty(value = "sku是否存在：0->不存在，1->存在")
    private Integer isSkuExist;
}
