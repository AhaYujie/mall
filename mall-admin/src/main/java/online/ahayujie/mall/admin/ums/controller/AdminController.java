package online.ahayujie.mall.admin.ums.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import online.ahayujie.mall.admin.ums.bean.dto.*;
import online.ahayujie.mall.admin.ums.bean.model.Admin;
import online.ahayujie.mall.admin.ums.bean.model.Role;
import online.ahayujie.mall.admin.ums.exception.DuplicateUsernameException;
import online.ahayujie.mall.admin.ums.exception.IllegalAdminStatusException;
import online.ahayujie.mall.admin.ums.service.AdminService;
import online.ahayujie.mall.admin.ums.service.RoleService;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.common.api.Result;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 后台用户表 前端控制器
 * </p>
 *
 * @author aha
 * @since 2020-06-04
 */
@RestController
@RequestMapping("/admin")
@Api(tags = "后台用户模块-用户管理")
public class AdminController {
    private final RoleService roleService;
    private final AdminService adminService;

    public AdminController(RoleService roleService, AdminService adminService) {
        this.roleService = roleService;
        this.adminService = adminService;
    }

    @ApiOperation(value = "用户注册")
    @PostMapping(value = "/register")
    public Result<Admin> register(@Valid @RequestBody AdminRegisterParam param) {
        try {
            Admin admin = adminService.register(param);
            return Result.data(admin);
        } catch (DuplicateUsernameException e) {
            return Result.fail("用户名已存在");
        }
    }

    @ApiOperation(value = "用户登录")
    @PostMapping(value = "/login")
    public Result<AdminLoginDTO> login(@Valid @RequestBody AdminLoginParam param) {
        try {
            return Result.data(adminService.login(param));
        } catch (UsernameNotFoundException | BadCredentialsException e) {
            return Result.fail("用户名或密码错误");
        } catch (DisabledException e) {
            return Result.fail("用户被禁用");
        }
    }

    @ApiOperation(value = "刷新accessToken", notes = "用refreshToken刷新accessToken")
    @PostMapping("/refreshAccessToken")
    public Result<AdminLoginDTO> refreshAccessToken(@RequestParam String refreshToken) {
        try {
            AdminLoginDTO adminLoginDTO = adminService.refreshAccessToken(refreshToken);
            return Result.data(adminLoginDTO);
        } catch (IllegalArgumentException e) {
            return Result.fail("refreshToken不合法");
        }
    }

    @ApiOperation(value = "给用户分配角色")
    @PostMapping("/role/update")
    public Result<Object> updateRole(@RequestParam("adminId") Long adminId,
                                     @RequestParam("roleIds") List<Long> roleIds) {
        try {
            adminService.updateRole(adminId, roleIds);
            return Result.success();
        } catch (UsernameNotFoundException e) {
            return Result.fail("用户不存在");
        } catch (IllegalArgumentException e) {
            return Result.fail("角色不合法");
        }
    }

    @ApiOperation(value = "获取指定用户的角色")
    @GetMapping("/role/{adminId}")
    public Result<List<Role>> getAdminRoleList(@PathVariable Long adminId) {
        return Result.data(roleService.getRoleListByAdminId(adminId));
    }

    @ApiOperation(value = "获取当前登录用户信息")
    @GetMapping("/info")
    public Result<AdminInfoDTO> getAdminInfo() {
        return Result.data(adminService.getAdminInfo());
    }

    @ApiOperation(value = "根据用户名或昵称分页获取用户列表")
    @GetMapping("/query")
    public Result<CommonPage<Admin>> getAdminList(@RequestParam(required = false) String keyword,
                                                  @RequestParam(defaultValue = "5") Integer pageSize,
                                                  @RequestParam(defaultValue = "1") Integer pageNum) {
        return Result.data(adminService.getAdminList(keyword, pageNum, pageSize));
    }

    @ApiOperation(value = "分页获取用户列表")
    @GetMapping("list")
    public Result<CommonPage<Admin>> getAdminList(@RequestParam(defaultValue = "1", required = false) Long pageNum,
                                                  @RequestParam(defaultValue = "20", required = false) Long pageSize) {
        return Result.data(adminService.getAdminList(pageNum, pageSize));
    }

    @ApiOperation(value = "获取指定用户信息")
    @GetMapping("/{id}")
    public Result<Admin> getAdmin(@PathVariable Long id) {
        return Result.data(adminService.getById(id));
    }

    @ApiOperation(value = "修改指定用户信息")
    @PostMapping("/update/{id}")
    public Result<Object> updateAdmin(@PathVariable Long id, @RequestBody UpdateAdminParam param) {
        try {
            adminService.updateAdmin(id, param);
            return Result.success();
        } catch (DuplicateUsernameException e) {
            return Result.fail("用户名重复");
        } catch (IllegalAdminStatusException e) {
            return Result.fail("用户状态不合法");
        }
    }

    @ApiOperation(value = "修改指定用户密码")
    @PostMapping("/updatePassword")
    public Result<Object> updatePassword(@RequestBody @Valid UpdateAdminPasswordParam param) {
        try {
            adminService.updatePassword(param);
            return Result.success();
        } catch (UsernameNotFoundException e) {
            return Result.fail("用户不存在");
        } catch (BadCredentialsException e) {
            return Result.fail("原密码错误");
        }
    }

    @ApiOperation(value = "删除指定用户信息")
    @PostMapping("/delete/{id}")
    public Result<Object> deleteAdmin(@PathVariable Long id) {
        adminService.removeById(id);
        return Result.success();
    }

    @ApiOperation(value = "修改帐号状态")
    @PostMapping("/updateStatus/{id}")
    public Result<Object> updateAdminStatus(@PathVariable Long id, @RequestParam Integer status) {
        UpdateAdminParam param = new UpdateAdminParam();
        param.setStatus(status);
        return updateAdmin(id, param);
    }
}
