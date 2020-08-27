package online.ahayujie.mall.admin.oms.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import online.ahayujie.mall.admin.oms.bean.dto.OrderRefundApplyDetailDTO;
import online.ahayujie.mall.admin.oms.bean.model.OrderRefundApply;
import online.ahayujie.mall.admin.oms.service.OrderRefundApplyService;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.common.api.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 订单仅退款申请 前端控制器
 * </p>
 *
 * @author aha
 * @since 2020-08-08
 */
@RestController
@RequestMapping("/order-refund-apply")
@Api(tags = "订单模块", value = "订单仅退款申请管理")
public class OrderRefundApplyController {
    private final OrderRefundApplyService orderRefundApplyService;

    public OrderRefundApplyController(OrderRefundApplyService orderRefundApplyService) {
        this.orderRefundApplyService = orderRefundApplyService;
    }

    @ApiOperation(value = "分页获取订单仅退款申请")
    @GetMapping("list")
    public Result<CommonPage<OrderRefundApply>> list(@RequestParam(required = false, defaultValue = "1") Long pageNum,
                                                     @RequestParam(required = false, defaultValue = "20") Long pageSize) {
        return Result.data(orderRefundApplyService.list(pageNum, pageSize));
    }

    @ApiOperation(value = "分页查询订单仅退款申请")
    @GetMapping("query")
    public Result<CommonPage<OrderRefundApply>> query(@RequestParam(required = false, defaultValue = "1") Long pageNum,
                                                      @RequestParam(required = false, defaultValue = "20") Long pageSize,
                                                      @RequestParam(required = false) String orderSn,
                                                      @RequestParam(required = false) Integer status) {
        return Result.data(orderRefundApplyService.query(pageNum, pageSize, orderSn, status));
    }

    @ApiOperation(value = "查询某一订单的仅退款申请")
    @GetMapping("/order/query")
    public Result<List<OrderRefundApply>> queryByOrderId(@RequestParam Long orderId) {
        return Result.data(orderRefundApplyService.queryByOrderId(orderId));
    }

    @ApiOperation(value = "获取仅退款申请详情")
    @GetMapping("/{id}")
    public Result<OrderRefundApplyDetailDTO> getDetailById(@PathVariable Long id) {
        return Result.data(orderRefundApplyService.getDetailById(id));
    }
}
