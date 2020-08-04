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
 * 商品审核记录
 * </p>
 *
 * @author aha
 * @since 2020-08-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pms_product_verify_log")
@ApiModel(value="ProductVerifyLog对象", description="商品审核记录")
public class ProductVerifyLog extends Base {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品id")
    private Long productId;

    @ApiModelProperty(value = "操作用户id")
    private Long adminId;

    @ApiModelProperty(value = "操作用户名")
    private String username;

    @ApiModelProperty(value = "审核备注")
    private String note;

    @ApiModelProperty(value = "审核后状态：0->未审核；1->审核通过")
    private Integer isVerify;


}
