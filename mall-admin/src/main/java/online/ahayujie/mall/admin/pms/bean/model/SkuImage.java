package online.ahayujie.mall.admin.pms.bean.model;

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
 * sku图片
 * </p>
 *
 * @author aha
 * @since 2020-07-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pms_sku_image")
@ApiModel(value="SkuImage对象", description="sku图片")
public class SkuImage extends Base {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "sku主键")
    private Long skuId;

    @ApiModelProperty(value = "图片url")
    private String image;


}
