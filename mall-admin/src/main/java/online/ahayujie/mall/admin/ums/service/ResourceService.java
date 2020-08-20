package online.ahayujie.mall.admin.ums.service;

import online.ahayujie.mall.admin.ums.bean.dto.CreateResourceParam;
import online.ahayujie.mall.admin.ums.bean.dto.UpdateResourceParam;
import online.ahayujie.mall.admin.ums.bean.model.Resource;
import com.baomidou.mybatisplus.extension.service.IService;
import online.ahayujie.mall.admin.ums.bean.model.Role;
import online.ahayujie.mall.admin.ums.exception.IllegalResourceCategoryException;
import online.ahayujie.mall.admin.ums.exception.IllegalResourceException;
import org.springframework.context.ApplicationEventPublisherAware;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 后台资源表 服务类
 * </p>
 *
 * @author aha
 * @since 2020-06-04
 */
public interface ResourceService extends ApplicationEventPublisherAware {
    /**
     * 创建资源
     * 若categoryId参数为null，则默认设置为0，即没有分类
     * @param param 资源
     * @throws IllegalResourceCategoryException 资源分类不存在
     */
    void createResource(CreateResourceParam param) throws IllegalResourceCategoryException;

    /**
     * 更新资源
     * @param id 资源id
     * @param param 资源
     * @throws IllegalResourceCategoryException 资源分类不存在
     */
    void updateResource(Long id, UpdateResourceParam param) throws IllegalResourceCategoryException;

    /**
     * 根据角色id获取角色拥有的资源，返回的资源是没有重复的
     * roleIds为null则抛出NPE，为空则返回空列表
     * @param roleIds 角色id
     * @return 用户拥有的资源
     */
    List<Resource> getResourceListByRoleIds(List<Long> roleIds);

    /**
     * 判断资源合法性。
     * 若资源不存在和不合法。
     * @param resourceIds 资源id
     * @throws IllegalResourceException 资源不合法
     */
    void validateResource(Collection<Long> resourceIds) throws IllegalResourceException;

    /**
     * 判断资源合法性
     *
     * @see #validateResource(Collection)
     * @param resourceId 资源id
     * @throws IllegalResourceException 资源不合法
     */
    void validateResource(Long resourceId) throws IllegalResourceException;

    /**
     * 获取全部资源，如果没有资源则返回空列表
     * @return 资源
     */
    List<Resource> list();

    /**
     * 根据id获取资源
     * @param id 主键id
     * @return 资源
     */
    Resource getById(Long id);

    /**
     * 根据id删除资源
     * 删除资源成功后用Spring事件机制发布
     * {@link online.ahayujie.mall.admin.ums.event.DeleteResourceEvent} 事件
     * @param id 资源id
     * @return 删除的资源数量
     */
    int removeById(Long id);

    /**
     * 根据分类id获取资源
     * @param categoryId 分类id
     * @return 资源
     */
    List<Resource> getByCategoryId(Long categoryId);
}
