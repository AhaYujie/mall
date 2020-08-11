package online.ahayujie.mall.admin.oms.bean.model;

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
 * 退货原因表
 * </p>
 *
 * @author aha
 * @since 2020-08-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("oms_order_return_reason")
@ApiModel(value="OrderReturnReason对象", description="退货原因表")
public class OrderReturnReason extends Base {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "退货原因名称")
    private String name;

    @ApiModelProperty(value = "排序，从大到小")
    private Integer sort;

    @ApiModelProperty(value = "状态：0->不启用；1->启用")
    private Boolean status;


}
