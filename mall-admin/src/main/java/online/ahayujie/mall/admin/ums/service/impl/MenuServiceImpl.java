package online.ahayujie.mall.admin.ums.service.impl;

import online.ahayujie.mall.admin.ums.bean.model.Menu;
import online.ahayujie.mall.admin.ums.exception.admin.IllegalMenuException;
import online.ahayujie.mall.admin.ums.mapper.MenuMapper;
import online.ahayujie.mall.admin.ums.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import online.ahayujie.mall.common.bean.model.Base;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
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
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
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
}
