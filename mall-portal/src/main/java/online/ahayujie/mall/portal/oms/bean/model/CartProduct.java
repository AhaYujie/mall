package online.ahayujie.mall.portal.oms.bean.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import online.ahayujie.mall.common.bean.model.Base;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 购物车商品表
 * </p>
 *
 * @author aha
 * @since 2020-10-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("oms_cart_product")
@ApiModel(value="CartProduct对象", description="购物车商品表")
public class CartProduct extends Base {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品的id")
    private Long productId;

    @ApiModelProperty(value = "商品sku的id")
    private Long skuId;

    @ApiModelProperty(value = "商品品牌id")
    private Long brandId;

    @ApiModelProperty(value = "商品分类id")
    private Long productCategoryId;

    @ApiModelProperty(value = "会员id")
    private Long memberId;

    @ApiModelProperty(value = "会员用户名")
    private String memberUsername;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "商品品牌名称")
    private String brandName;

    @ApiModelProperty(value = "商品分类名称")
    private String productCategoryName;

    @ApiModelProperty(value = "商品货号")
    private String productSn;

    @ApiModelProperty(value = "商品sku编码")
    private String skuCode;

    @ApiModelProperty(value = "商品图片")
    private String skuPic;

    @ApiModelProperty(value = "商品规格（json格式）")
    private String specification;

    @ApiModelProperty(value = "购买数量")
    private Integer quantity;

}
