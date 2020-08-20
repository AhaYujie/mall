package online.ahayujie.mall.admin.ums.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author aha
 * @since 2020/8/20
 */
@Data
public class AdminRoleRelationDTO {
    @ApiModelProperty(value = "后台用户id")
    private Long adminId;

    @ApiModelProperty(value = "角色id")
    private Long roleId;
}
