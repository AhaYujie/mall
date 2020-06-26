package online.ahayujie.mall.admin.ums.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import online.ahayujie.mall.admin.ums.bean.dto.CreateMenuParam;
import online.ahayujie.mall.admin.ums.bean.dto.MenuNodeDTO;
import online.ahayujie.mall.admin.ums.bean.dto.UpdateMenuParam;
import online.ahayujie.mall.admin.ums.bean.model.Menu;
import online.ahayujie.mall.admin.ums.exception.admin.IllegalMenuException;
import online.ahayujie.mall.admin.ums.exception.admin.IllegalMenuVisibilityException;
import online.ahayujie.mall.admin.ums.exception.admin.IllegalParentMenuException;
import online.ahayujie.mall.admin.ums.service.MenuService;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.common.api.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 后台菜单表 前端控制器
 * </p>
 *
 * @author aha
 * @since 2020-06-04
 */
@RestController
@RequestMapping("/menu")
@Api(tags = "后台用户管理", value = "后台菜单管理")
public class MenuController {
    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @ApiOperation(value = "添加后台菜单")
    @PostMapping("/create")
    public Result<Object> createMenu(@RequestBody CreateMenuParam param) {
        try {
            menuService.createMenu(param);
            return Result.success();
        } catch (IllegalParentMenuException e) {
            return Result.fail("上级菜单不存在");
        } catch (IllegalMenuVisibilityException e) {
            return Result.fail("菜单可见性不合法");
        }
    }

    @ApiOperation(value = "修改后台菜单")
    @PostMapping("/update/{id}")
    public Result<Object> updateMenu(@PathVariable Long id,  @RequestBody UpdateMenuParam param) {
        try {
            menuService.updateMenu(id, param);
            return Result.success();
        } catch (IllegalParentMenuException e) {
            return Result.fail("上级菜单不存在");
        } catch (IllegalMenuVisibilityException e) {
            return Result.fail("菜单可见性不合法");
        } catch (IllegalMenuException e) {
            return Result.fail("菜单不存在");
        }
    }

    @ApiOperation(value = "根据ID获取菜单详情")
    @GetMapping("/{id}")
    public Result<Menu> getMenuById(@PathVariable Long id) {
        return Result.data(menuService.getById(id));
    }

    @ApiOperation(value = "根据ID删除后台菜单")
    @PostMapping("/delete/{id}")
    public Result<Object> deleteById(@PathVariable Long id) {
        menuService.removeById(id);
        return Result.success();
    }

    @ApiOperation(value = "分页查询后台菜单")
    @GetMapping("/list/{parentId}")
    public Result<CommonPage<Menu>> queryByParentId(@PathVariable Long parentId,
                                                    @RequestParam(defaultValue = "5") Integer pageSize,
                                                    @RequestParam(defaultValue = "1") Integer pageNum) {
        return Result.data(menuService.queryByParentId(parentId, pageSize, pageNum));
    }

    @ApiOperation(value = "树形结构返回所有菜单列表")
    @GetMapping("/treeList")
    public Result<List<MenuNodeDTO>> getTreeList() {
        return Result.data(menuService.getTreeList());
    }

    @ApiOperation(value = "修改菜单显示状态")
    @PostMapping("/updateHidden/{id}")
    public Result<Object> updateMenuHidden(@PathVariable Long id, @RequestParam Integer hidden) {
        UpdateMenuParam param = new UpdateMenuParam();
        param.setHidden(hidden);
        return updateMenu(id, param);
    }
}
