package online.ahayujie.mall.admin.pms.bean.model;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.ToString;
import online.ahayujie.mall.common.bean.model.Base;
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
 * @since 2020-07-14
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pms_product")
@ApiModel(value="Product对象", description="商品信息")
public class Product extends Base {
    public static final Integer UNIT_MAX_LENGTH = 5;

    public enum PromotionType {
        /**
         * 没有促销使用原价
         */
        ORIGIN_PRICE(0),

        /**
         * 使用促销价
         */
        PROMOTION_PRICE(1),

        /**
         * 使用会员价
         */
        MEMBER_PRICE(2),

        /**
         * 使用阶梯价格
         */
        LADDER_PRICE(3),

        /**
         * 使用满减价格
         */
        FULL_REDUCTION(4),

        /**
         * 限时购
         */
        FLASH_PROMOTION(5)
        ;
        private final Integer value;

        PromotionType(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    public enum PublishStatus {
        /**
         * 未上架
         */
        NOT_PUBLISH(0),

        /**
         * 已上架
         */
        PUBLISH(1)
        ;
        private final Integer value;

        PublishStatus(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    public enum NewStatus {
        /**
         * 不是新品
         */
        NOT_NEW(0),

        /**
         * 新品
         */
        NEW(1)
        ;
        private final Integer value;

        NewStatus(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    public enum RecommendStatus {
        /**
         * 不推荐
         */
        NOT_RECOMMEND(0),

        /**
         * 推荐
         */
        RECOMMEND(1)
        ;
        private final Integer value;

        RecommendStatus(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    public enum VerifyStatus {
        /**
         * 未审核
         */
        NOT_VERIFY(0),

        /**
         * 已审核
         */
        VERIFY(1)
        ;
        private final Integer value;

        VerifyStatus(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    public enum PreviewStatus {
        /**
         * 不是预告商品
         */
        NOT_PREVIEW(0),

        /**
         * 是预告商品
         */
        PREVIEW(1)
        ;
        private final Integer value;

        PreviewStatus(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

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

    @ApiModelProperty(value = "图片，用于列表显示")
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

    @ApiModelProperty(value = "计量单位")
    private String unit;

    @ApiModelProperty(value = "商品备注")
    private String note;

    @ApiModelProperty(value = "商品关键词")
    private String keywords;

    @ApiModelProperty(value = "排序，从大到小")
    private Integer sort;

    @ApiModelProperty(value = "赠送的成长值")
    private Integer giftGrowth;

    @ApiModelProperty(value = "赠送的积分")
    private Integer giftPoint;

    @ApiModelProperty(value = "限制使用的积分数")
    private Integer usePointLimit;

    @ApiModelProperty(value = "以逗号分割的产品服务：1->无忧退货；2->快速退款；3->免费包邮")
    private String serviceIds;

    @ApiModelProperty(value = "促销类型：0->没有促销使用原价;1->使用促销价；2->使用会员价；3->使用阶梯价格；4->使用满减价格；5->限时购")
    private Integer promotionType;

    @ApiModelProperty(value = "删除状态：0->未删除；1->已删除")
    private Integer isDeleted;

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


}
