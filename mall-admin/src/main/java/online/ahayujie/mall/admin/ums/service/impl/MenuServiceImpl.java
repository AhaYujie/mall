package online.ahayujie.mall.admin.ums.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.admin.ums.bean.dto.CreateMenuParam;
import online.ahayujie.mall.admin.ums.bean.dto.MenuNodeDTO;
import online.ahayujie.mall.admin.ums.bean.dto.UpdateMenuParam;
import online.ahayujie.mall.admin.ums.bean.model.Menu;
import online.ahayujie.mall.admin.ums.exception.IllegalMenuException;
import online.ahayujie.mall.admin.ums.exception.IllegalMenuVisibilityException;
import online.ahayujie.mall.admin.ums.exception.IllegalParentMenuException;
import online.ahayujie.mall.admin.ums.mapper.MenuMapper;
import online.ahayujie.mall.admin.ums.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.common.bean.model.Base;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 后台菜单表 服务实现类
 * </p>
 *
 * @author aha
 * @since 2020-06-04
 */
@Service
public class MenuServiceImpl implements MenuService {
    private final MenuMapper menuMapper;

    public MenuServiceImpl(MenuMapper menuMapper) {
        this.menuMapper = menuMapper;
    }

    @Override
    public void validateMenu(Collection<Long> menuIds) throws IllegalMenuException {
        List<Long> legalMenuIds = list().stream().map(Base::getId).collect(Collectors.toList());
        for (Long menuId : menuIds) {
            if (!legalMenuIds.contains(menuId)) {
                throw new IllegalMenuException("菜单id不合法: " + menuId);
            }
        }
    }

    @Override
    public void validateMenu(Long menuId) throws IllegalMenuException {
        validateMenu(Collections.singletonList(menuId));
    }

    @Override
    public void createMenu(CreateMenuParam param) throws IllegalParentMenuException, IllegalMenuVisibilityException {
        validateMenuParentId(param.getParentId());
        validateMenuVisibility(param.getHidden());
        Menu menu = new Menu();
        BeanUtils.copyProperties(param, menu);
        menu.setLevel(getMenuLevel(param.getParentId()));
        menu.setCreateTime(new Date());
        menuMapper.insert(menu);
    }

    @Override
    public void updateMenu(Long id, UpdateMenuParam param) throws IllegalMenuException,
            IllegalParentMenuException, IllegalMenuVisibilityException {
        validateMenu(id);
        Menu menu = new Menu();
        BeanUtils.copyProperties(param, menu);
        if (param.getParentId() != null) {
            validateMenuParentId(param.getParentId());
            menu.setLevel(getMenuLevel(param.getParentId()));
        }
        if (param.getHidden() != null) {
            validateMenuVisibility(param.getHidden());
        }
        menu.setId(id);
        menu.setUpdateTime(new Date());
        menuMapper.updateById(menu);
    }

    @Override
    public CommonPage<Menu> queryByParentId(Long parentId, Integer pageSize, Integer pageNum) {
        Page<Menu> page = new Page<>(pageNum, pageSize);
        IPage<Menu> menus = menuMapper.selectByParentId(page, parentId);
        return new CommonPage<>(menus);
    }

    @Override
    public List<MenuNodeDTO> getTreeList() {
        List<Menu> menus = list();
        return convertToMenuTree(menus);
    }

    @Override
    public Menu getById(Long id) {
        return menuMapper.selectById(id);
    }

    @Override
    public List<Menu> list() {
        return menuMapper.selectAll();
    }

    @Override
    public int removeById(Long id) {
        // TODO:处理与被删除的菜单关联的其他数据
        return menuMapper.deleteById(id);
    }

    private List<MenuNodeDTO> convertToMenuTree(List<Menu> menus) {
        List<MenuNodeDTO> menuTree = new ArrayList<>();
        Set<MenuNodeDTO> nodes = new HashSet<>();
        List<Menu> copy = new ArrayList<>(menus);
        while (!copy.isEmpty()) {
            for (Menu menu : copy) {
                if (Menu.NON_PARENT_ID == menu.getParentId()) {
                    MenuNodeDTO menuNodeDTO = new MenuNodeDTO();
                    BeanUtils.copyProperties(menu, menuNodeDTO);
                    menuNodeDTO.setChildren(new ArrayList<>());
                    menuTree.add(menuNodeDTO);
                    nodes.add(menuNodeDTO);
                    copy.remove(menu);
                    break;
                }
                boolean found = false;
                for (MenuNodeDTO node : nodes) {
                    if (node.getId().equals(menu.getParentId())) {
                        MenuNodeDTO child = new MenuNodeDTO();
                        BeanUtils.copyProperties(menu, child);
                        child.setChildren(new ArrayList<>());
                        node.getChildren().add(child);
                        nodes.add(child);
                        copy.remove(menu);
                        found = true;
                        break;
                    }
                }
                if (found) {
                    break;
                }
            }
        }
        return menuTree;
    }

    private int getMenuLevel(long parentId) {
        if (Menu.NON_PARENT_ID == parentId) {
            return 0;
        }
        Menu parentMenu = menuMapper.selectById(parentId);
        return parentMenu.getLevel() + 1;
    }

    private void validateMenuParentId(Long parentId) throws IllegalParentMenuException {
        if (parentId == null) {
            throw new IllegalParentMenuException("parentId is null");
        }
        if (parentId != Menu.NON_PARENT_ID && menuMapper.selectById(parentId) == null) {
            throw new IllegalParentMenuException("上级菜单不存在");
        }
    }

    private void validateMenuVisibility(Integer hidden) throws IllegalMenuVisibilityException {
        boolean exist = false;
        for (Menu.VISIBILITY visibility : Menu.VISIBILITY.values()) {
            if (visibility.getValue().equals(hidden)) {
                exist = true;
                break;
            }
        }
        if (!exist) {
            throw new IllegalMenuVisibilityException("菜单可见性不合法");
        }
    }
}
