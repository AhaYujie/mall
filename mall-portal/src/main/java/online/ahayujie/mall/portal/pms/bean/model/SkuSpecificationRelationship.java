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
 * sku和商品规格关系
 * </p>
 *
 * @author aha
 * @since 2020-10-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pms_sku_specification_relationship")
@ApiModel(value="SkuSpecificationRelationship对象", description="sku和商品规格关系")
public class SkuSpecificationRelationship extends Base {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "sku主键")
    private Long skuId;

    @ApiModelProperty(value = "商品规格id")
    private Long specificationId;

    @ApiModelProperty(value = "商品规格选项id")
    private Long specificationValueId;


}
