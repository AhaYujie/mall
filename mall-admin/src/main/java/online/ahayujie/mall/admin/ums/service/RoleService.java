package online.ahayujie.mall.admin.ums.service;

import online.ahayujie.mall.admin.ums.bean.dto.CreateRoleParam;
import online.ahayujie.mall.admin.ums.bean.dto.UpdateRoleParam;
import online.ahayujie.mall.admin.ums.bean.model.Menu;
import online.ahayujie.mall.admin.ums.bean.model.Resource;
import online.ahayujie.mall.admin.ums.bean.model.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import online.ahayujie.mall.admin.ums.event.DeleteAdminEvent;
import online.ahayujie.mall.admin.ums.event.DeleteMenuEvent;
import online.ahayujie.mall.admin.ums.event.DeleteResourceEvent;
import online.ahayujie.mall.admin.ums.exception.IllegalMenuException;
import online.ahayujie.mall.admin.ums.exception.IllegalResourceException;
import online.ahayujie.mall.admin.ums.exception.IllegalRoleException;
import online.ahayujie.mall.common.api.CommonPage;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
public interface RoleService {
    /**
     * 根据用户id获取用户拥有的角色, 且角色状态是启用的
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
     * 如果 ids 是 null 则不做处理
     * 删除角色成功后，会删除相关的后台用户角色关系，菜单角色关系，资源角色关系
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

    /**
     * 获取全部角色，如果没有角色则返回空列表
     * @return 角色
     */
    List<Role> list();

    /**
     * 根据id获取角色
     * @param id 主键id
     * @return 角色
     */
    Role getById(Long id);

    /**
     * 监听删除后台用户事件
     * @param deleteAdminEvent 删除后台用户事件
     */
    void listenDeleteAdminEvent(DeleteAdminEvent deleteAdminEvent);

    /**
     * 更新后台用户的角色，
     * 如果 roleIdList 为 null，则不做任何处理，
     * 如果 roleIdList 为空，则删除用户的所有角色
     * @param adminId 用户id
     * @param roleIdList 角色id
     * @throws IllegalRoleException 角色id不合法
     */
    void updateAdminRole(Long adminId, List<Long> roleIdList) throws IllegalRoleException;

    /**
     * 监听删除菜单事件
     * @param deleteMenuEvent 删除菜单事件
     */
    void listenDeleteMenuEvent(DeleteMenuEvent deleteMenuEvent);

    /**
     * 监听删除资源事件
     * @param deleteResourceEvent 删除资源事件
     */
    void listenDeleteResourceEvent(DeleteResourceEvent deleteResourceEvent);

    /**
     * 获取全部启用的角色
     * @return 全部启用的角色
     */
    List<Role> getActiveRoles();
}
