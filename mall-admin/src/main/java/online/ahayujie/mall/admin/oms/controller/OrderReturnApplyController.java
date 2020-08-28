package online.ahayujie.mall.admin.oms.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import online.ahayujie.mall.admin.oms.bean.dto.OrderReturnApplyDetailDTO;
import online.ahayujie.mall.admin.oms.bean.dto.RefuseOrderReturnApplyParam;
import online.ahayujie.mall.admin.oms.bean.model.OrderReturnApply;
import online.ahayujie.mall.admin.oms.exception.IllegalOrderReturnApplyException;
import online.ahayujie.mall.admin.oms.service.OrderReturnApplyService;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.common.api.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 订单退货退款申请 前端控制器
 * </p>
 *
 * @author aha
 * @since 2020-08-08
 */
@RestController
@RequestMapping("/order-return-apply")
@Api(tags = "订单模块", value = "订单退货退款申请管理")
public class OrderReturnApplyController {
    private final OrderReturnApplyService orderReturnApplyService;

    public OrderReturnApplyController(OrderReturnApplyService orderReturnApplyService) {
        this.orderReturnApplyService = orderReturnApplyService;
    }

    @ApiOperation(value = "分页获取订单退货退款申请")
    @GetMapping("list")
    public Result<CommonPage<OrderReturnApply>> list(@RequestParam(required = false, defaultValue = "1") Long pageNum,
                                                     @RequestParam(required = false, defaultValue = "20") Long pageSize) {
        return Result.data(orderReturnApplyService.list(pageNum, pageSize));
    }

    @ApiOperation(value = "分页查询订单退货退款申请")
    @GetMapping("query")
    public Result<CommonPage<OrderReturnApply>> query(@RequestParam(required = false, defaultValue = "1") Long pageNum,
                                                      @RequestParam(required = false, defaultValue = "20") Long pageSize,
                                                      @RequestParam(required = false) String orderSn,
                                                      @RequestParam(required = false) Integer status) {
        return Result.data(orderReturnApplyService.query(pageNum, pageSize, orderSn, status));
    }

    @ApiOperation(value = "查询某一订单的退货退款申请")
    @GetMapping("/order/query")
    public Result<List<OrderReturnApply>> queryByOrderId(@RequestParam Long orderId) {
        return Result.data(orderReturnApplyService.queryByOrderId(orderId));
    }

    @ApiOperation(value = "获取退货退款申请详情")
    @GetMapping("/{id}")
    public Result<OrderReturnApplyDetailDTO> getDetailById(@PathVariable Long id) {
        return Result.data(orderReturnApplyService.getDetailById(id));
    }

    @ApiOperation(value = "拒绝退货退款申请")
    @PostMapping("refuse")
    public Result<Object> refuseApply(@RequestBody RefuseOrderReturnApplyParam param) {
        try {
            orderReturnApplyService.refuseApply(param);
            return Result.success();
        } catch (IllegalOrderReturnApplyException e) {
            return Result.fail(e.getResultCode(), e.getMessage());
        }
    }
}
