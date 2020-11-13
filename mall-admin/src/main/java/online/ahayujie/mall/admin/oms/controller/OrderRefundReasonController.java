package online.ahayujie.mall.admin.oms.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import online.ahayujie.mall.admin.oms.bean.dto.CreateOrderRefundReasonParam;
import online.ahayujie.mall.admin.oms.bean.dto.UpdateOrderRefundReasonParam;
import online.ahayujie.mall.admin.oms.bean.model.OrderRefundReason;
import online.ahayujie.mall.admin.oms.exception.IllegalOrderRefundReasonException;
import online.ahayujie.mall.admin.oms.service.OrderRefundReasonService;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.common.api.Result;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 订单仅退款原因 前端控制器
 * </p>
 *
 * @author aha
 * @since 2020-08-08
 */
@RestController
@RequestMapping("/order-refund-reason")
@Api(tags = "订单模块-订单仅退款原因管理")
public class OrderRefundReasonController {
    private final OrderRefundReasonService orderRefundReasonService;

    public OrderRefundReasonController(OrderRefundReasonService orderRefundReasonService) {
        this.orderRefundReasonService = orderRefundReasonService;
    }

    @ApiOperation(value = "新增订单仅退款原因")
    @PostMapping("create")
    public Result<Object> create(@RequestBody CreateOrderRefundReasonParam param) {
        try {
            orderRefundReasonService.create(param);
            return Result.success();
        } catch (IllegalOrderRefundReasonException e) {
            return Result.fail(e.getResultCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "更新订单仅退款原因")
    @PostMapping("/update/{id}")
    public Result<Object> update(@RequestBody UpdateOrderRefundReasonParam param, @PathVariable Long id) {
        try {
            orderRefundReasonService.update(param, id);
            return Result.success();
        } catch (IllegalOrderRefundReasonException e) {
            return Result.fail(e.getResultCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "删除订单仅退款原因")
    @PostMapping("/delete/{id}")
    public Result<Object> delete(@PathVariable Long id) {
        orderRefundReasonService.delete(id);
        return Result.success();
    }

    @ApiOperation(value = "分页获取订单仅退款原因")
    @GetMapping("list")
    public Result<CommonPage<OrderRefundReason>> list(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                      @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return Result.data(orderRefundReasonService.list(pageNum, pageSize));
    }
}
