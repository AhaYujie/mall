package online.ahayujie.mall.admin.ums.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author aha
 * @date 2020/6/25
 */
@Data
public class CreateRoleParam {
    @ApiModelProperty(value = "名称", required = true)
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "启用状态：0->禁用；1->启用，默认为1")
    private Integer status;

    @ApiModelProperty(value = "排序")
    private Integer sort;
}
