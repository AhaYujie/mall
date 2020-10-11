package online.ahayujie.mall.portal.mms.controller;


import io.swagger.annotations.ApiOperation;
import online.ahayujie.mall.common.api.Result;
import online.ahayujie.mall.common.api.ResultCode;
import online.ahayujie.mall.portal.mms.bean.dto.*;
import online.ahayujie.mall.portal.mms.exception.DuplicatePhoneException;
import online.ahayujie.mall.portal.mms.exception.DuplicateUsernameException;
import online.ahayujie.mall.portal.mms.service.MemberService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author aha
 * @since 2020-10-09
 */
@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @ApiOperation(value = "会员注册", notes = "会员注册，用户名已存在返回code=1，手机号已存在返回code=2，成功注册返回code=200")
    @PostMapping("register")
    public Result<Object> register(@RequestBody MemberRegisterParam param) {
        try {
            memberService.register(param);
            return Result.success();
        } catch (DuplicateUsernameException e) {
            return Result.fail(new ResultCode() {
                @Override
                public Integer getCode() {
                    return 1;
                }

                @Override
                public String getMessage() {
                    return "用户名已存在";
                }
            });
        } catch (DuplicatePhoneException e) {
            return Result.fail(new ResultCode() {
                @Override
                public Integer getCode() {
                    return 2;
                }

                @Override
                public String getMessage() {
                    return "手机号已存在";
                }
            });
        }
    }

    @ApiOperation(value = "会员登录", notes = "会员登录，用户名或密码错误返回code=1，用户被禁用返回code=2，登录成功返回code=200")
    @PostMapping("login")
    public Result<MemberLoginDTO> login(@RequestBody MemberLoginParam param) {
        try {
            return Result.data(memberService.login(param));
        } catch (UsernameNotFoundException | BadCredentialsException e) {
            return Result.fail(new ResultCode() {
                @Override
                public Integer getCode() {
                    return 1;
                }

                @Override
                public String getMessage() {
                    return "用户名或密码错误";
                }
            });
        } catch (DisabledException e) {
            return Result.fail(new ResultCode() {
                @Override
                public Integer getCode() {
                    return 2;
                }

                @Override
                public String getMessage() {
                    return "用户被禁用";
                }
            });
        }
    }

    @ApiOperation(value = "刷新accessToken")
    @PostMapping("refresh-access-token")
    public Result<MemberLoginDTO> refreshAccessToken(@RequestParam String refreshToken) {
        try {
            return Result.data(memberService.refreshAccessToken(refreshToken));
        } catch (IllegalArgumentException e) {
            return Result.fail("refreshToken不合法");
        }
    }

    @ApiOperation(value = "获取会员信息")
    @GetMapping("info")
    public Result<MemberDTO> getInfo() {
        return Result.data(memberService.getInfo());
    }

    @ApiOperation(value = "更新会员信息")
    @PostMapping("/info/update")
    public Result<Object> updateInfo(@RequestBody UpdateMemberParam param) {
        try {
            memberService.updateInfo(param);
            return Result.success();
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }
}
