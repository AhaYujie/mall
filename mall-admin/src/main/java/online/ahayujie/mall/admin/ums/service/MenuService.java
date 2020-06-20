package online.ahayujie.mall.admin.ums.service;

import online.ahayujie.mall.admin.ums.bean.model.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import online.ahayujie.mall.admin.ums.exception.admin.IllegalMenuException;

import java.util.Collection;

/**
 * <p>
 * 后台菜单表 服务类
 * </p>
 *
 * @author aha
 * @since 2020-06-04
 */
public interface MenuService extends IService<Menu> {
    /**
     * 判断菜单合法性
     * @param menuIds 菜单id
     * @throws IllegalMenuException 菜单不合法
     */
    void validateMenu(Collection<Long> menuIds) throws IllegalMenuException;

    /**
     * 判断菜单合法性
     * @param menuId 菜单id
     * @throws IllegalMenuException 菜单不合法
     */
    void validateMenu(Long menuId) throws IllegalMenuException;
}
