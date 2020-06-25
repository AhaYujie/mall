package online.ahayujie.mall.admin.ums.service;

import online.ahayujie.mall.admin.ums.bean.dto.CreateRoleParam;
import online.ahayujie.mall.admin.ums.bean.dto.UpdateRoleParam;
import online.ahayujie.mall.admin.ums.bean.model.Menu;
import online.ahayujie.mall.admin.ums.bean.model.Resource;
import online.ahayujie.mall.admin.ums.bean.model.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import online.ahayujie.mall.admin.ums.exception.admin.IllegalMenuException;
import online.ahayujie.mall.admin.ums.exception.admin.IllegalResourceException;
import online.ahayujie.mall.admin.ums.exception.admin.IllegalRoleException;
import online.ahayujie.mall.common.api.CommonPage;

import java.util.Collection;
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

    /**
     * 添加角色
     * @param param 角色信息
     */
    void createRole(CreateRoleParam param);

    /**
     * 根据 id 更新角色
     * 可以更新角色除了 id 和 adminCount 的其他数据
     * @param id 角色 id
     * @param param 更新的角色信息
     * @throws IllegalArgumentException 角色信息不合法
     * @throws IllegalRoleException 角色不存在
     */
    void updateRole(Long id, UpdateRoleParam param) throws IllegalArgumentException, IllegalRoleException;

    /**
     * 批量删除角色
     * 如果 code 是 null 则不做处理
     * @param ids 角色id
     */
    void deleteRoles(List<Long> ids);

    /**
     * 分页获取角色列表
     * @param keyword 角色名称关键词
     * @param pageSize 页大小
     * @param pageNum 页索引
     * @return 角色列表
     */
    CommonPage<Role> list(String keyword, Integer pageSize, Integer pageNum);

    /**
     * 获取角色相关菜单
     * @param id 角色id
     * @return 菜单
     */
    List<Menu> listMenu(Long id);

    /**
     * 获取角色相关资源
     * @param id 角色id
     * @return 资源
     */
    List<Resource> listResource(Long id);

    /**
     * 分配角色相关菜单，
     * menuIds 为 null 则不做操作，
     * menuIds 为空则删除角色全部相关菜单
     * @param roleId 角色id
     * @param menuIds 菜单id
     * @throws IllegalRoleException 角色id不合法
     * @throws IllegalMenuException 菜单id不合法
     */
    void updateRoleMenu(Long roleId, List<Long> menuIds) throws IllegalRoleException, IllegalMenuException;

    /**
     * 分配角色相关资源，
     * resourceIds 为 null 不做任何操作，
     * resourceIds 为空则删除角色全部相关资源
     * @param roleId 角色id
     * @param resourceIds 资源id
     * @throws IllegalRoleException 角色不合法
     * @throws IllegalResourceException 资源不合法
     */
    void updateRoleResource(Long roleId, List<Long> resourceIds) throws IllegalRoleException, IllegalResourceException;

    /**
     * 判断角色合法性
     * @param roleIds 角色id
     * @throws IllegalRoleException 角色不合法
     */
    void validateRole(Collection<Long> roleIds) throws IllegalRoleException;

    /**
     * 判断角色合法性
     * 调用 {@link #validateRole(Collection)} 进行判断
     * @param roleId 角色id
     * @throws IllegalRoleException 角色不合法
     */
    void validateRole(Long roleId) throws IllegalRoleException;
}
