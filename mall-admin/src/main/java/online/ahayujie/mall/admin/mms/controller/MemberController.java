package online.ahayujie.mall.admin.mms.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import online.ahayujie.mall.admin.mms.bean.model.LoginLog;
import online.ahayujie.mall.admin.mms.bean.model.Member;
import online.ahayujie.mall.admin.mms.service.MemberService;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.common.api.Result;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author aha
 * @since 2020-08-20
 */
@RestController
@RequestMapping("/member")
@Api(tags = "会员管理", value = "会员管理")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @ApiOperation(value = "分页获取会员列表")
    @GetMapping("list")
    public Result<CommonPage<Member>> list(@RequestParam(required = false, defaultValue = "1") Long pageNum,
                                           @RequestParam(required = false, defaultValue = "20") Long pageSize) {
        return Result.data(memberService.list(pageNum, pageSize));
    }

    @ApiOperation(value = "根据用户名右模糊查询会员")
    @GetMapping("/query/username")
    public Result<CommonPage<Member>> queryByUsername(@RequestParam(required = false, defaultValue = "1") Long pageNum,
                                                      @RequestParam(required = false, defaultValue = "20") Long pageSize,
                                                      @RequestParam String username) {
        return Result.data(memberService.queryByUsername(pageNum, pageSize, username));
    }

    @ApiOperation(value = "根据手机号右模糊查询会员")
    @GetMapping("/query/phone")
    public Result<CommonPage<Member>> queryByPhone(@RequestParam(required = false, defaultValue = "1") Long pageNum,
                                                   @RequestParam(required = false, defaultValue = "20") Long pageSize,
                                                   @RequestParam String phone) {
        return Result.data(memberService.queryByPhone(pageNum, pageSize, phone));
    }
    
    @ApiOperation(value = "分页获取会员的登录记录")
    @GetMapping("/login-log/{id}")
    public Result<CommonPage<LoginLog>> getLoginLog(@RequestParam(required = false, defaultValue = "1") Long pageNum,
                                                    @RequestParam(required = false, defaultValue = "20") Long pageSize,
                                                    @PathVariable Long id) {
        return Result.data(memberService.getLoginLog(pageNum, pageSize, id));
    }
}
