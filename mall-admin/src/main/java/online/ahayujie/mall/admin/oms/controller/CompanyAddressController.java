package online.ahayujie.mall.admin.oms.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import online.ahayujie.mall.admin.oms.bean.dto.CreateCompanyAddressParam;
import online.ahayujie.mall.admin.oms.bean.dto.UpdateCompanyAddressParam;
import online.ahayujie.mall.admin.oms.bean.model.CompanyAddress;
import online.ahayujie.mall.admin.oms.exception.IllegalCompanyAddressException;
import online.ahayujie.mall.admin.oms.service.CompanyAddressService;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.common.api.Result;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 公司收发货地址表 前端控制器
 * </p>
 *
 * @author aha
 * @since 2020-08-08
 */
@RestController
@RequestMapping("/company-address")
@Api(tags = "订单模块", value = "公司地址管理")
public class CompanyAddressController {
    private final CompanyAddressService companyAddressService;

    public CompanyAddressController(CompanyAddressService companyAddressService) {
        this.companyAddressService = companyAddressService;
    }

    @ApiOperation(value = "新建公司地址")
    @PostMapping("create")
    public Result<Object> create(@RequestBody CreateCompanyAddressParam param) {
        try {
            companyAddressService.create(param);
            return Result.success();
        } catch (IllegalCompanyAddressException e) {
            return Result.fail(e.getResultCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "更新公司地址")
    @PostMapping("/update/{id}")
    public Result<Object> update(@PathVariable Long id, @RequestBody UpdateCompanyAddressParam param) {
        try {
            companyAddressService.update(id, param);
            return Result.success();
        } catch (IllegalCompanyAddressException e) {
            return Result.fail(e.getResultCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "删除公司地址")
    @PostMapping("/delete/{id}")
    public Result<Object> delete(@PathVariable Long id) {
        companyAddressService.delete(id);
        return Result.success();
    }

    @ApiOperation(value = "分页获取公司地址")
    @GetMapping("list")
    public Result<CommonPage<CompanyAddress>> list(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                   @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return Result.data(companyAddressService.list(pageNum, pageSize));
    }
}
