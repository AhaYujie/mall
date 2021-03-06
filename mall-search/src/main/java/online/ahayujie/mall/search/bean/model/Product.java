package online.ahayujie.mall.search.bean.model;

import java.math.BigDecimal;
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
 * 商品信息
 * </p>
 *
 * @author aha
 * @since 2020-10-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pms_product")
@ApiModel(value="Product对象", description="商品信息")
public class Product extends Base {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品品牌id，0表示没有品牌")
    private Long brandId;

    @ApiModelProperty(value = "商品分类id，0表示没有分类")
    private Long productCategoryId;

    @ApiModelProperty(value = "货号")
    private String productSn;

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

    @ApiModelProperty(value = "销量")
    private Integer sale;

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

    @ApiModelProperty(value = "删除状态：0->未删除；1->已删除")
    private Integer isDeleted;

    @ApiModelProperty(value = "上架状态：0->下架；1->上架，默认为0")
    private Integer isPublish;

    @ApiModelProperty(value = "新品状态:0->不是新品；1->新品，默认为0")
    private Integer isNew;

    @ApiModelProperty(value = "推荐状态；0->不推荐；1->推荐，默认为0")
    private Integer isRecommend;

    @ApiModelProperty(value = "审核状态：0->未审核；1->审核通过，默认为0")
    private Integer isVerify;

    @ApiModelProperty(value = "是否为预告商品：0->不是；1->是，默认为0")
    private Integer isPreview;

}
