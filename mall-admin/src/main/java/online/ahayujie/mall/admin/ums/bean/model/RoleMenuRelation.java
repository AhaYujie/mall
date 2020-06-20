package online.ahayujie.mall.admin.ums.bean.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import online.ahayujie.mall.common.bean.model.Base;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 后台角色菜单关系表
 * </p>
 *
 * @author aha
 * @since 2020-06-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ums_role_menu_relation")
@ApiModel(value="RoleMenuRelation对象", description="后台角色菜单关系表")
public class RoleMenuRelation extends Base {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "是否删除，0->未删除，1->已删除")
    private Boolean isDeleted;

    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    @ApiModelProperty(value = "菜单ID")
    private Long menuId;

    public RoleMenuRelation(Long id, Date updateTime, Date createTime, Long roleId, Long menuId) {
        super(id, updateTime, createTime);
        this.roleId = roleId;
        this.menuId = menuId;
    }
}
