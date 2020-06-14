package online.ahayujie.mall.admin.ums.service.impl;

import online.ahayujie.mall.admin.ums.bean.model.Resource;
import online.ahayujie.mall.admin.ums.bean.model.Role;
import online.ahayujie.mall.admin.ums.bean.model.RoleResourceRelation;
import online.ahayujie.mall.admin.ums.mapper.ResourceMapper;
import online.ahayujie.mall.admin.ums.mapper.RoleResourceRelationMapper;
import online.ahayujie.mall.admin.ums.service.ResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import online.ahayujie.mall.admin.ums.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 后台资源表 服务实现类
 * </p>
 *
 * @author aha
 * @since 2020-06-04
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {
    private final ResourceMapper resourceMapper;
    private final RoleResourceRelationMapper roleResourceRelationMapper;

    private RoleService roleService;

    public ResourceServiceImpl(ResourceMapper resourceMapper, RoleResourceRelationMapper roleResourceRelationMapper) {
        this.resourceMapper = resourceMapper;
        this.roleResourceRelationMapper = roleResourceRelationMapper;
    }

    @Override
    public List<Resource> getResourceListByAdminId(Long adminId) {
        List<Role> roleList = roleService.getRoleListByAdminId(adminId);
        Set<RoleResourceRelation> roleResourceRelations = new HashSet<>();
        for (Role role : roleList) {
            roleResourceRelations.addAll(roleResourceRelationMapper.selectByRoleId(role.getId()));
        }
        return roleResourceRelations.stream()
                .map(relation -> resourceMapper.selectById(relation.getResourceId()))
                .collect(Collectors.toList());
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }
}
