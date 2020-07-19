package online.ahayujie.mall.admin.pms.bean.model;

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
 * 商品sku
 * </p>
 *
 * @author aha
 * @since 2020-07-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pms_sku")
@ApiModel(value="Sku对象", description="商品sku")
public class Sku extends Base {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品id")
    private Long productId;

    @ApiModelProperty(value = "sku编码")
    private String skuCode;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "单品促销价格")
    private BigDecimal promotionPrice;

    @ApiModelProperty(value = "库存")
    private Integer stock;

    @ApiModelProperty(value = "锁定库存")
    private Integer lockStock;

    @ApiModelProperty(value = "预警库存")
    private Integer lowStock;

    @ApiModelProperty(value = "销量")
    private Integer sale;

    @ApiModelProperty(value = "默认展示图片url")
    private String pic;

    @ApiModelProperty(value = "商品规格，json格式")
    private String specification;


}
