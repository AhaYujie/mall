package online.ahayujie.mall.admin.ums.service;

import online.ahayujie.mall.admin.ums.bean.dto.CreateResourceCategoryParam;
import online.ahayujie.mall.admin.ums.bean.dto.UpdateResourceCategoryParam;
import online.ahayujie.mall.admin.ums.bean.model.ResourceCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import online.ahayujie.mall.admin.ums.exception.IllegalResourceCategoryException;

import java.util.List;

/**
 * <p>
 * 资源分类表 服务类
 * </p>
 *
 * @author aha
 * @since 2020-06-04
 */
public interface ResourceCategoryService {
    /**
     * 获取全部资源分类，
     * 根据 sort 从大到小排序
     * @return 资源分类
     */
    List<ResourceCategory> listAll();

    /**
     * 创建资源分类
     * @param param 资源分类信息
     */
    void create(CreateResourceCategoryParam param);

    /**
     * 更新资源分类
     * @param id 资源分类id
     * @param param 资源分类信息
     * @throws IllegalResourceCategoryException 资源分类不存在
     */
    void update(Long id, UpdateResourceCategoryParam param) throws IllegalResourceCategoryException;

    /**
     * 删除资源分类，
     * 只删除资源分类，不删除该分类下的资源
     * @param id 资源分类id
     */
    void delete(Long id);

    /**
     * 根据id获取资源分类
     * @param id 主键id
     * @return 资源分类
     */
    ResourceCategory getById(Long id);
}
