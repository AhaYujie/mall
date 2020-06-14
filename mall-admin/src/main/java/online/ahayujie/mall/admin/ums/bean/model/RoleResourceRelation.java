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

import java.util.Objects;

/**
 * <p>
 * 后台角色资源关系表
 * </p>
 *
 * @author aha
 * @since 2020-06-08
 */
@Data
@Accessors(chain = true)
@TableName("ums_role_resource_relation")
@ApiModel(value="RoleResourceRelation对象", description="后台角色资源关系表")
public class RoleResourceRelation extends Base {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "是否删除，0->未删除，1->已删除")
    private Boolean isDeleted;

    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    @ApiModelProperty(value = "资源ID")
    private Long resourceId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoleResourceRelation)) {
            return false;
        }
        RoleResourceRelation that = (RoleResourceRelation) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getCreateTime(), that.getCreateTime()) &&
                Objects.equals(getUpdateTime(), that.getUpdateTime()) &&
                Objects.equals(isDeleted, that.isDeleted) &&
                Objects.equals(roleId, that.roleId) &&
                Objects.equals(resourceId, that.resourceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCreateTime(), getUpdateTime(), isDeleted, roleId, resourceId);
    }
}
