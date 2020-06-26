package online.ahayujie.mall.admin.ums.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import online.ahayujie.mall.admin.ums.bean.model.Role;

import java.util.Date;
import java.util.List;

/**
 * @author aha
 * @date 2020/6/26
 */
@Data
public class AdminInfoDTO {
    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "头像")
    private String icon;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "备注信息")
    private String note;

    @ApiModelProperty(value = "最后登录时间")
    private Date loginTime;

    @ApiModelProperty(value = "帐号启用状态：0->禁用；1->启用")
    private Integer status;

    @ApiModelProperty(value = "角色")
    private List<Role> roles;
}
