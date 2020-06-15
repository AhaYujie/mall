package online.ahayujie.mall.admin.ums.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import online.ahayujie.mall.admin.ums.bean.dto.AdminLoginDTO;
import online.ahayujie.mall.admin.ums.bean.dto.AdminLoginParam;
import online.ahayujie.mall.admin.ums.bean.dto.AdminRegisterParam;
import online.ahayujie.mall.admin.ums.bean.model.Admin;
import online.ahayujie.mall.admin.ums.exception.admin.DuplicateUsernameException;
import online.ahayujie.mall.admin.ums.service.AdminService;
import online.ahayujie.mall.common.api.Result;
import org.springframework.security.authentication.BadCredentialsException;
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
@Api(tags = "后台用户管理", value = "后台用户管理")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
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

}
