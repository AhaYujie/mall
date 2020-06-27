package online.ahayujie.mall.admin.ums.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author aha
 * @date 2020/6/27
 */
@Data
public class UpdateAdminParam {
    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "头像")
    private String icon;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "备注信息")
    private String note;

    @ApiModelProperty(value = "帐号启用状态：0->禁用；1->启用")
    private Integer status;
}
