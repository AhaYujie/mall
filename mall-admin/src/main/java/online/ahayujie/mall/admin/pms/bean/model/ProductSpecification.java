package online.ahayujie.mall.admin.pms.bean.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.ToString;
import online.ahayujie.mall.common.bean.model.Base;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品规格
 * </p>
 *
 * @author aha
 * @since 2020-07-14
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pms_product_specification")
@ApiModel(value="ProductSpecification对象", description="商品规格")
public class ProductSpecification extends Base {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品id")
    private Long productId;

    @ApiModelProperty(value = "规格名称")
    private String name;


}
