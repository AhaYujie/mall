package online.ahayujie.mall.admin.ums.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import online.ahayujie.mall.admin.ums.bean.dto.CreateRoleParam;
import online.ahayujie.mall.admin.ums.bean.dto.UpdateRoleParam;
import online.ahayujie.mall.admin.ums.bean.model.Menu;
import online.ahayujie.mall.admin.ums.bean.model.Resource;
import online.ahayujie.mall.admin.ums.bean.model.Role;
import online.ahayujie.mall.admin.ums.exception.IllegalMenuException;
import online.ahayujie.mall.admin.ums.exception.IllegalResourceException;
import online.ahayujie.mall.admin.ums.exception.IllegalRoleException;
import online.ahayujie.mall.admin.ums.service.RoleService;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.common.api.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 后台用户角色表 前端控制器
 * </p>
 *
 * @author aha
 * @since 2020-06-04
 */
@RestController
@RequestMapping("/role")
@Api(tags = "后台用户模块-角色管理")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @ApiOperation(value = "添加角色")
    @PostMapping("/create")
    public Result<Object> createRole(@RequestBody CreateRoleParam param) {
        roleService.createRole(param);
        return Result.success();
    }

    @ApiOperation(value = "修改角色")
    @PostMapping("/update/{id}")
    public Result<Object> updateRole(@PathVariable Long id, @RequestBody UpdateRoleParam param) {
        try {
            roleService.updateRole(id, param);
            return Result.success();
        } catch (IllegalArgumentException e) {
            return Result.fail("角色信息不合法");
        } catch (IllegalRoleException e) {
            return Result.fail("角色不存在");
        }
    }

    @ApiOperation(value = "批量删除角色")
    @PostMapping("/delete")
    public Result<Object> deleteRoles(@RequestParam List<Long> ids) {
        roleService.deleteRoles(ids);
        return Result.success();
    }

    @ApiOperation(value = "获取所有角色")
    @GetMapping("/listAll")
    public Result<List<Role>> listAll() {
        return Result.data(roleService.list());
    }

    @ApiOperation(value = "根据角色名称分页获取角色列表")
    @GetMapping("/list")
    public Result<CommonPage<Role>> queryByName(@RequestParam(required = false) String keyword,
                                                @RequestParam(defaultValue = "5") Integer pageSize,
                                                @RequestParam(defaultValue = "1") Integer pageNum) {
        return Result.data(roleService.list(keyword, pageSize, pageNum));
    }

    @ApiOperation(value = "修改角色状态")
    @PostMapping("/updateStatus/{id}")
    public Result<Object> updateRoleStatus(@PathVariable Long id, @RequestParam Integer status) {
        UpdateRoleParam param = new UpdateRoleParam();
        param.setStatus(status);
        try {
            roleService.updateRole(id, param);
            return Result.success();
        } catch (IllegalArgumentException e) {
            return Result.fail("角色信息不合法");
        } catch (IllegalRoleException e) {
            return Result.fail("角色不存在");
        }
    }

    @ApiOperation(value = "获取角色相关菜单")
    @GetMapping("/listMenu/{roleId}")
    public Result<List<Menu>> listMenu(@PathVariable Long roleId) {
        return Result.data(roleService.listMenu(roleId));
    }

    @ApiOperation(value = "获取角色相关资源")
    @GetMapping("/listResource/{roleId}")
    public Result<List<Resource>> listResource(@PathVariable Long roleId) {
        return Result.data(roleService.listResource(roleId));
    }

    @ApiOperation(value = "给角色分配菜单")
    @PostMapping("/allocMenu")
    public Result<Object> updateRoleMenu(@RequestParam Long roleId, @RequestParam List<Long> menuIds) {
        try {
            roleService.updateRoleMenu(roleId, menuIds);
            return Result.success();
        } catch (IllegalRoleException e) {
            return Result.fail("角色不存在");
        } catch (IllegalMenuException e) {
            return Result.fail("菜单不存在");
        }
    }

    @ApiOperation(value = "给角色分配资源")
    @PostMapping("/allocResource")
    public Result<Object> updateRoleResource(@RequestParam Long roleId, @RequestParam List<Long> resourceIds) {
        try {
            roleService.updateRoleResource(roleId, resourceIds);
            return Result.success();
        } catch (IllegalRoleException e) {
            return Result.fail("角色不存在");
        } catch (IllegalResourceException e) {
            return Result.fail("资源不存在");
        }
    }
}
