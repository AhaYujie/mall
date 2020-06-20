package online.ahayujie.mall.admin.ums.service;

import online.ahayujie.mall.admin.ums.bean.model.Resource;
import com.baomidou.mybatisplus.extension.service.IService;
import online.ahayujie.mall.admin.ums.exception.admin.IllegalResourceException;

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
public interface ResourceService extends IService<Resource> {
    /**
     * 根据用户id获取用户拥有的资源
     * @param adminId 用户id
     * @return 用户拥有的资源
     */
    List<Resource> getResourceListByAdminId(Long adminId);

    /**
     * 判断资源合法性
     * @param resourceIds 资源id
     * @throws IllegalResourceException 资源不合法
     */
    void validateResource(Collection<Long> resourceIds) throws IllegalResourceException;

    /**
     * 判断资源合法性
     * @param resourceId 资源id
     * @throws IllegalResourceException 资源不合法
     */
    void validateResource(Long resourceId) throws IllegalResourceException;
}
