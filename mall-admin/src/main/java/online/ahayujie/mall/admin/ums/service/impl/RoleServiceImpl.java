package online.ahayujie.mall.admin.ums.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.ums.bean.dto.CreateRoleParam;
import online.ahayujie.mall.admin.ums.bean.dto.UpdateRoleParam;
import online.ahayujie.mall.admin.ums.bean.model.*;
import online.ahayujie.mall.admin.ums.event.DeleteAdminEvent;
import online.ahayujie.mall.admin.ums.event.DeleteMenuEvent;
import online.ahayujie.mall.admin.ums.event.DeleteResourceEvent;
import online.ahayujie.mall.admin.ums.exception.IllegalMenuException;
import online.ahayujie.mall.admin.ums.exception.IllegalResourceException;
import online.ahayujie.mall.admin.ums.exception.IllegalRoleException;
import online.ahayujie.mall.admin.ums.mapper.AdminRoleRelationMapper;
import online.ahayujie.mall.admin.ums.mapper.RoleMapper;
import online.ahayujie.mall.admin.ums.mapper.RoleMenuRelationMapper;
import online.ahayujie.mall.admin.ums.mapper.RoleResourceRelationMapper;
import online.ahayujie.mall.admin.ums.service.MenuService;
import online.ahayujie.mall.admin.ums.service.ResourceService;
import online.ahayujie.mall.admin.ums.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.common.bean.model.Base;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 后台用户角色表 服务实现类
 * </p>
 *
 * @author aha
 * @since 2020-06-04
 */
@Slf4j
@Service
public class RoleServiceImpl implements RoleService {
    private final RoleMapper roleMapper;
    private final RoleMenuRelationMapper roleMenuRelationMapper;
    private final AdminRoleRelationMapper adminRoleRelationMapper;
    private final RoleResourceRelationMapper roleResourceRelationMapper;

    private MenuService menuService;
    private ResourceService resourceService;

    public RoleServiceImpl(RoleMapper roleMapper, RoleMenuRelationMapper roleMenuRelationMapper,
                           AdminRoleRelationMapper adminRoleRelationMapper, RoleResourceRelationMapper roleResourceRelationMapper) {
        this.roleMapper = roleMapper;
        this.roleMenuRelationMapper = roleMenuRelationMapper;
        this.adminRoleRelationMapper = adminRoleRelationMapper;
        this.roleResourceRelationMapper = roleResourceRelationMapper;
    }

    @Override
    public List<Role> getRoleListByAdminId(Long adminId) {
        List<AdminRoleRelation> adminRoleRelations = adminRoleRelationMapper.selectByAdminId(adminId);
        return adminRoleRelations.stream()
                .map(relation -> roleMapper.selectById(relation.getRoleId()))
                .filter(role -> role.getStatus().equals(Role.STATUS.ACTIVE.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public void createRole(CreateRoleParam param) {
        Role role = new Role();
        BeanUtils.copyProperties(param, role);
        role.setCreateTime(new Date());
        role.setAdminCount(0);
        roleMapper.insert(role);
    }

    @Override
    public void updateRole(Long id, UpdateRoleParam param) throws IllegalArgumentException {
        Role role = new Role();
        role.setId(id);
        BeanUtils.copyProperties(param, role);
        List<Integer> roleStatus = Arrays.stream(Role.STATUS.values())
                .map(Role.STATUS::getValue).collect(Collectors.toList());
        if (role.getStatus() != null && !roleStatus.contains(role.getStatus())) {
            throw new IllegalArgumentException("角色状态不合法");
        }
        role.setUpdateTime(new Date());
        int count = roleMapper.updateById(role);
        if (count == 0) {
            throw new IllegalRoleException("角色不存在");
        }
    }

    @Override
    public void deleteRoles(List<Long> ids) {
        // TODO:处理与被删除的角色关联的数据
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        roleMapper.deleteBatchIds(ids);
    }

    @Override
    public CommonPage<Role> list(String keyword, Integer pageSize, Integer pageNum) {
        if (StringUtils.isEmpty(keyword)) {
            keyword = "";
        }
        Page<Role> page = new Page<>(pageNum, pageSize);
        IPage<Role> rolePage = roleMapper.queryByName(page, keyword);
        log.debug("total: " + rolePage.getTotal());
        log.debug("getPages: " + rolePage.getPages());
        return new CommonPage<>(rolePage);
    }

    @Override
    public List<Menu> listMenu(Long id) {
        return roleMapper.selectMenusByRoleId(id);
    }

    @Override
    public List<Resource> listResource(Long id) {
        return roleMapper.selectResourceByRoleId(id);
    }

    @Override
    public void updateRoleMenu(Long roleId, List<Long> menuIds) throws IllegalRoleException, IllegalMenuException {
        if (menuIds == null) {
            return;
        }
        validateRole(roleId);
        menuService.validateMenu(menuIds);
        roleMenuRelationMapper.deleteByRoleId(roleId);
        List<RoleMenuRelation> relations = menuIds.stream()
                .map(menuId -> new RoleMenuRelation(null, null, new Date(), roleId, menuId))
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(relations)) {
            roleMenuRelationMapper.insert(relations);
        }
    }

    @Override
    public void updateRoleResource(Long roleId, List<Long> resourceIds) throws IllegalRoleException, IllegalResourceException {
        if (resourceIds == null) {
            return;
        }
        validateRole(roleId);
        resourceService.validateResource(resourceIds);
        roleResourceRelationMapper.deleteByRoleId(roleId);
        List<RoleResourceRelation> relations = resourceIds.stream()
                .map(resourceId -> new RoleResourceRelation(null, null, new Date(), roleId, resourceId))
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(relations)) {
            roleResourceRelationMapper.insert(relations);
        }
    }

    @Override
    public void validateRole(Collection<Long> roleIds) throws IllegalRoleException {
        List<Long> legalRoleIds = list().stream().map(Base::getId).collect(Collectors.toList());
        for (Long roleId : roleIds) {
            if (!legalRoleIds.contains(roleId)) {
                throw new IllegalRoleException("角色id不合法: " + roleId);
            }
        }
    }

    @Override
    public void validateRole(Long roleId) throws IllegalRoleException {
        validateRole(Collections.singletonList(roleId));
    }

    @Override
    public List<Role> list() {
        return roleMapper.selectAll();
    }

    @Override
    public Role getById(Long id) {
        return roleMapper.selectById(id);
    }

    @Async
    @Override
    @EventListener
    public void listenDeleteAdminEvent(DeleteAdminEvent deleteAdminEvent) {
        adminRoleRelationMapper.deleteByAdminId(deleteAdminEvent.getSource().getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAdminRole(Long adminId, List<Long> roleIdList) throws UsernameNotFoundException, IllegalRoleException {
        if (roleIdList == null) {
            return;
        }
        // 检查角色是否合法
        validateRole(roleIdList);
        // 删除用户原本的全部角色
        adminRoleRelationMapper.deleteByAdminId(adminId);
        // 添加新角色
        List<AdminRoleRelation> adminRoleRelations = roleIdList.stream()
                .map(roleId -> new AdminRoleRelation(null, null, new Date(), adminId, roleId))
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(adminRoleRelations)) {
            adminRoleRelationMapper.insert(adminRoleRelations);
        }
    }

    @Async
    @Override
    @EventListener
    public void listenDeleteMenuEvent(DeleteMenuEvent deleteMenuEvent) {
        roleMenuRelationMapper.deleteByMenuId(deleteMenuEvent.getSource().getId());
    }

    @Async
    @Override
    @EventListener
    public void listenDeleteResourceEvent(DeleteResourceEvent deleteResourceEvent) {
        roleResourceRelationMapper.deleteByResourceId(deleteResourceEvent.getSource().getId());
    }

    @Autowired
    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }

    @Autowired
    public void setResourceService(ResourceService resourceService) {
        this.resourceService = resourceService;
    }
}
