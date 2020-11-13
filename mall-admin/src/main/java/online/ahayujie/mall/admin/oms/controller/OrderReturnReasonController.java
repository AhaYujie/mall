package online.ahayujie.mall.admin.oms.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import online.ahayujie.mall.admin.oms.bean.dto.CreateOrderReturnReasonParam;
import online.ahayujie.mall.admin.oms.bean.dto.UpdateOrderReturnReasonParam;
import online.ahayujie.mall.admin.oms.bean.model.OrderReturnReason;
import online.ahayujie.mall.admin.oms.exception.IllegalOrderReturnReasonException;
import online.ahayujie.mall.admin.oms.service.OrderReturnReasonService;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.common.api.Result;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 退货原因表 前端控制器
 * </p>
 *
 * @author aha
 * @since 2020-08-08
 */
@RestController
@RequestMapping("/order-return-reason")
@Api(tags = "订单模块-订单退货退款原因管理")
public class OrderReturnReasonController {
    private final OrderReturnReasonService orderReturnReasonService;

    public OrderReturnReasonController(OrderReturnReasonService orderReturnReasonService) {
        this.orderReturnReasonService = orderReturnReasonService;
    }

    @ApiOperation(value = "新增订单退货退款原因")
    @PostMapping("create")
    public Result<Object> create(@RequestBody CreateOrderReturnReasonParam param) {
        try {
            orderReturnReasonService.create(param);
            return Result.success();
        } catch (IllegalOrderReturnReasonException e) {
            return Result.fail(e.getResultCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "更新订单退货退款原因")
    @PostMapping("/update/{id}")
    public Result<Object> update(@PathVariable Long id, @RequestBody UpdateOrderReturnReasonParam param) {
        try {
            orderReturnReasonService.update(id, param);
            return Result.success();
        } catch (IllegalOrderReturnReasonException e) {
            return Result.fail(e.getResultCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "删除订单退货退款原因")
    @PostMapping("/delete/{id}")
    public Result<Object> delete(@PathVariable Long id) {
        orderReturnReasonService.delete(id);
        return Result.success();
    }

    @ApiOperation(value = "分页获取订单退货退款原因")
    @GetMapping("list")
    public Result<CommonPage<OrderReturnReason>> list(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                      @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return Result.data(orderReturnReasonService.list(pageNum, pageSize));
    }
}
