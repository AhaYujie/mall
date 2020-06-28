package online.ahayujie.mall.admin.ums.service;

import online.ahayujie.mall.admin.ums.bean.dto.CreateMenuParam;
import online.ahayujie.mall.admin.ums.bean.dto.MenuNodeDTO;
import online.ahayujie.mall.admin.ums.bean.dto.UpdateMenuParam;
import online.ahayujie.mall.admin.ums.bean.model.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import online.ahayujie.mall.admin.ums.exception.IllegalMenuException;
import online.ahayujie.mall.admin.ums.exception.IllegalMenuVisibilityException;
import online.ahayujie.mall.admin.ums.exception.IllegalParentMenuException;
import online.ahayujie.mall.common.api.CommonPage;

import java.util.Collection;
import java.util.List;

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

    /**
     * 创建菜单
     * @param param 菜单信息
     * @throws IllegalParentMenuException 父级菜单不合法
     * @throws IllegalMenuVisibilityException 菜单可见性不合法
     */
    void createMenu(CreateMenuParam param) throws IllegalParentMenuException, IllegalMenuVisibilityException;

    /**
     * 更新菜单
     * @param id 菜单id
     * @param param 菜单信息
     * @throws IllegalMenuException 菜单不存在
     * @throws IllegalParentMenuException 父级菜单不合法
     * @throws IllegalMenuVisibilityException 菜单可见性不合法
     */
    void updateMenu(Long id, UpdateMenuParam param) throws IllegalMenuException, IllegalParentMenuException,
            IllegalMenuVisibilityException;

    /**
     * 根据上级菜单id分页查询
     * @param parentId 上级菜单id
     * @param pageSize 单页大小
     * @param pageNum 页索引
     * @return 菜单
     */
    CommonPage<Menu> queryByParentId(Long parentId, Integer pageSize, Integer pageNum);

    /**
     * 获取树形结构的全部菜单
     * @return 菜单
     */
    List<MenuNodeDTO> getTreeList();
}
