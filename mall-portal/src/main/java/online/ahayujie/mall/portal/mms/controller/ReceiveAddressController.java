package online.ahayujie.mall.portal.mms.controller;


import io.swagger.annotations.ApiOperation;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.common.api.Result;
import online.ahayujie.mall.portal.mms.bean.dto.CreateReceiveAddressParam;
import online.ahayujie.mall.portal.mms.bean.dto.ReceiveAddressDTO;
import online.ahayujie.mall.portal.mms.bean.dto.UpdateReceiveAddressParam;
import online.ahayujie.mall.portal.mms.service.ReceiveAddressService;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 会员收货地址表 前端控制器
 * </p>
 *
 * @author aha
 * @since 2020-10-10
 */
@RestController
@RequestMapping("/receive-address")
public class ReceiveAddressController {
    private final ReceiveAddressService receiveAddressService;

    public ReceiveAddressController(ReceiveAddressService receiveAddressService) {
        this.receiveAddressService = receiveAddressService;
    }

    @ApiOperation(value = "分页获取会员的收货地址", notes = "分页获取会员的收货地址，默认地址在首位")
    @GetMapping("list")
    public Result<CommonPage<ReceiveAddressDTO>> list(@RequestParam(required = false, defaultValue = "1") Long pageNum,
                                                      @RequestParam(required = false, defaultValue = "20") Long pageSize) {
        return Result.data(receiveAddressService.list(pageNum, pageSize));
    }

    @ApiOperation(value = "新增收货地址", notes = "新增收货地址，如果设置新地址为默认地址且原本已经存在默认地址，则旧的默认地址变为非默认")
    @PostMapping("create")
    public Result<Object> create(@RequestBody CreateReceiveAddressParam param) {
        receiveAddressService.create(param);
        return Result.success();
    }

    @ApiOperation(value = "更新收货地址", notes = "更新收货地址，如果设置地址为默认地址且原本已经存在默认地址，则旧的默认地址变为非默认")
    @PostMapping("update")
    public Result<Object> update(@RequestBody UpdateReceiveAddressParam param) {
        try {
            receiveAddressService.update(param);
            return Result.success();
        } catch (IllegalArgumentException e) {
            return Result.fail("收货地址不存在");
        }
    }

    @ApiOperation(value = "删除收货地址")
    @PostMapping("delete")
    public Result<Object> delete(@RequestParam Long id) {
        receiveAddressService.delete(id);
        return Result.success();
    }

    @ApiOperation(value = "获取会员的默认收货地址", notes = "获取会员的默认收货地址，如果不存在则返回null")
    @GetMapping("default")
    public Result<ReceiveAddressDTO> getDefault() {
        return Result.data(receiveAddressService.getDefault());
    }
}
