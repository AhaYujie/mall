package online.ahayujie.mall.admin.ums.service.impl;

import online.ahayujie.mall.admin.ums.bean.dto.CreateResourceParam;
import online.ahayujie.mall.admin.ums.bean.dto.UpdateResourceParam;
import online.ahayujie.mall.admin.ums.bean.model.Resource;
import online.ahayujie.mall.admin.ums.bean.model.Role;
import online.ahayujie.mall.admin.ums.bean.model.RoleResourceRelation;
import online.ahayujie.mall.admin.ums.exception.IllegalResourceCategoryException;
import online.ahayujie.mall.admin.ums.exception.IllegalResourceException;
import online.ahayujie.mall.admin.ums.mapper.ResourceMapper;
import online.ahayujie.mall.admin.ums.mapper.RoleResourceRelationMapper;
import online.ahayujie.mall.admin.ums.service.ResourceCategoryService;
import online.ahayujie.mall.admin.ums.service.ResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import online.ahayujie.mall.admin.ums.service.RoleService;
import online.ahayujie.mall.common.bean.model.Base;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
public class ResourceServiceImpl implements ResourceService {
    private final ResourceMapper resourceMapper;
    private final RoleResourceRelationMapper roleResourceRelationMapper;

    private RoleService roleService;
    private ResourceCategoryService resourceCategoryService;

    public ResourceServiceImpl(ResourceMapper resourceMapper, RoleResourceRelationMapper roleResourceRelationMapper) {
        this.resourceMapper = resourceMapper;
        this.roleResourceRelationMapper = roleResourceRelationMapper;
    }

    @Override
    public void createResource(CreateResourceParam param) throws IllegalResourceCategoryException {
        Long categoryId = param.getCategoryId();
        if (categoryId != null && resourceCategoryService.getById(categoryId) == null) {
            throw new IllegalResourceCategoryException("资源分类不存在");
        }
        Resource resource = new Resource();
        BeanUtils.copyProperties(param, resource);
        resource.setCreateTime(new Date());
        resourceMapper.insert(resource);
    }

    @Override
    public void updateResource(Long id, UpdateResourceParam param) throws IllegalResourceCategoryException {
        Long categoryId = param.getCategoryId();
        if (categoryId != null && resourceCategoryService.getById(categoryId) == null) {
            throw new IllegalResourceCategoryException("资源分类不存在");
        }
        Resource resource = new Resource();
        BeanUtils.copyProperties(param, resource);
        resource.setId(id);
        resource.setUpdateTime(new Date());
        resourceMapper.updateById(resource);
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

    @Override
    public void validateResource(Collection<Long> resourceIds) throws IllegalResourceException {
        List<Long> legalResourceIds = list().stream().map(Base::getId).collect(Collectors.toList());
        for (Long resourceId : resourceIds) {
            if (!legalResourceIds.contains(resourceId)) {
                throw new IllegalResourceException("资源不合法");
            }
        }
    }

    @Override
    public void validateResource(Long resourceId) throws IllegalResourceException {
        validateResource(Collections.singletonList(resourceId));
    }

    @Override
    public List<Resource> list() {
        return resourceMapper.selectAll();
    }

    @Override
    public Resource getById(Long id) {
        return resourceMapper.selectById(id);
    }

    @Override
    public int removeById(Long id) {
        // TODO:处理与被删除的资源关联的数据
        return resourceMapper.deleteById(id);
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setResourceCategoryService(ResourceCategoryService resourceCategoryService) {
        this.resourceCategoryService = resourceCategoryService;
    }
}
