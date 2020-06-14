package online.ahayujie.mall.admin.ums.service;

import online.ahayujie.mall.admin.ums.bean.model.Resource;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
