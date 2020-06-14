package online.ahayujie.mall.admin.ums.service;

import online.ahayujie.mall.admin.ums.bean.model.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 后台用户角色表 服务类
 * </p>
 *
 * @author aha
 * @since 2020-06-04
 */
public interface RoleService extends IService<Role> {
    /**
     * 根据用户id获取用户拥有的角色
     * @param adminId 用户id
     * @return 用户拥有的角色
     */
    List<Role> getRoleListByAdminId(Long adminId);
}
