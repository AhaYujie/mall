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
import org.springframework.context.ApplicationEventPublisherAware;

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
public interface MenuService extends ApplicationEventPublisherAware {
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

    /**
     * 根据id获取菜单
     * @param id 主键id
     * @return 菜单
     */
    Menu getById(Long id);

    /**
     * 获取全部菜单，若没有菜单则返回空列表
     * @return 全部菜单
     */
    List<Menu> list();

    /**
     * 根据id删除菜单，如果该菜单存在下级菜单，则一并删除下级菜单。
     * 删除菜单成功后，通过Spring 事件机制发布
     * {@link online.ahayujie.mall.admin.ums.event.DeleteMenuEvent} 事件
     * @param id 主键id
     * @return 删除菜单的数量
     */
    int removeById(Long id);
}
