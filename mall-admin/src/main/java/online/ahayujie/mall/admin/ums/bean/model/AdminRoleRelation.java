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

/**
 * <p>
 * 后台用户和角色关系表
 * </p>
 *
 * @author aha
 * @since 2020-06-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ums_admin_role_relation")
@ApiModel(value="AdminRoleRelation对象", description="后台用户和角色关系表")
public class AdminRoleRelation extends Base {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "后台用户id")
    private Long adminId;

    @ApiModelProperty(value = "角色id")
    private Long roleId;


}
