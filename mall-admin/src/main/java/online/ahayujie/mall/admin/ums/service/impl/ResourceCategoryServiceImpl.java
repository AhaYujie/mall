package online.ahayujie.mall.admin.ums.service.impl;

import online.ahayujie.mall.admin.ums.bean.dto.CreateResourceCategoryParam;
import online.ahayujie.mall.admin.ums.bean.dto.UpdateResourceCategoryParam;
import online.ahayujie.mall.admin.ums.bean.model.ResourceCategory;
import online.ahayujie.mall.admin.ums.exception.admin.IllegalResourceCategoryException;
import online.ahayujie.mall.admin.ums.mapper.ResourceCategoryMapper;
import online.ahayujie.mall.admin.ums.service.ResourceCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class ResourceCategoryServiceImpl extends ServiceImpl<ResourceCategoryMapper, ResourceCategory> implements ResourceCategoryService {
    private final ResourceCategoryMapper resourceCategoryMapper;

    public ResourceCategoryServiceImpl(ResourceCategoryMapper resourceCategoryMapper) {
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
    }
}
