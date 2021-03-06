package online.ahayujie.mall.admin.ums.service.impl;

import online.ahayujie.mall.admin.ums.bean.dto.CreateResourceCategoryParam;
import online.ahayujie.mall.admin.ums.bean.dto.UpdateResourceCategoryParam;
import online.ahayujie.mall.admin.ums.bean.model.Resource;
import online.ahayujie.mall.admin.ums.bean.model.ResourceCategory;
import online.ahayujie.mall.admin.ums.exception.IllegalResourceCategoryException;
import online.ahayujie.mall.admin.ums.mapper.ResourceCategoryMapper;
import online.ahayujie.mall.admin.ums.mapper.ResourceMapper;
import online.ahayujie.mall.admin.ums.service.ResourceCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 资源分类表 服务实现类
 * </p>
 *
 * @author aha
 * @since 2020-06-04
 */
@Service
public class ResourceCategoryServiceImpl implements ResourceCategoryService {
    private final ResourceMapper resourceMapper;
    private final ResourceCategoryMapper resourceCategoryMapper;

    public ResourceCategoryServiceImpl(ResourceMapper resourceMapper, ResourceCategoryMapper resourceCategoryMapper) {
        this.resourceMapper = resourceMapper;
        this.resourceCategoryMapper = resourceCategoryMapper;
    }

    @Override
    public List<ResourceCategory> listAll() {
        return resourceCategoryMapper.selectAll();
    }

    @Override
    public void create(CreateResourceCategoryParam param) {
        ResourceCategory resourceCategory = new ResourceCategory();
        BeanUtils.copyProperties(param, resourceCategory);
        resourceCategory.setCreateTime(new Date());
        resourceCategoryMapper.insert(resourceCategory);
    }

    @Override
    public void update(Long id, UpdateResourceCategoryParam param) throws IllegalResourceCategoryException {
        ResourceCategory resourceCategory = new ResourceCategory();
        resourceCategory.setId(id);
        BeanUtils.copyProperties(param, resourceCategory);
        resourceCategory.setUpdateTime(new Date());
        if (resourceCategoryMapper.updateById(resourceCategory) <= 0) {
            throw new IllegalResourceCategoryException("资源分类不存在");
        }
    }

    @Override
    public void delete(Long id) {
        resourceCategoryMapper.deleteById(id);
        resourceMapper.deleteCategoryByCategoryId(id);
    }

    @Override
    public ResourceCategory getById(Long id) {
        return resourceCategoryMapper.selectById(id);
    }
}
