package online.ahayujie.mall.portal.pms.bean.model;

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
 * 商品规格选项
 * </p>
 *
 * @author aha
 * @since 2020-10-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pms_product_specification_value")
@ApiModel(value="ProductSpecificationValue对象", description="商品规格选项")
public class ProductSpecificationValue extends Base {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品规格id")
    private Long productSpecificationId;

    @ApiModelProperty(value = "选项值")
    private String value;

    @ApiModelProperty(value = "选项类型：0->纯文本，1->图文")
    private Integer type;


}
