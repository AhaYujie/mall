package online.ahayujie.mall.admin.oms.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import online.ahayujie.mall.admin.oms.bean.dto.CreateOrderParam;
import online.ahayujie.mall.admin.oms.bean.dto.OrderDetailDTO;
import online.ahayujie.mall.admin.oms.bean.dto.OrderListDTO;
import online.ahayujie.mall.admin.oms.bean.dto.QueryOrderListParam;
import online.ahayujie.mall.admin.oms.exception.IllegalOrderException;
import online.ahayujie.mall.admin.oms.service.OrderService;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.common.api.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author aha
 * @since 2020-08-08
 */
@RestController
@RequestMapping("/order")
@Api(tags = "订单模块", value = "订单管理")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @ApiOperation(value = "分页查询订单列表", notes = "分页查询订单列表。查询参数中某一字段为null则忽略不作为查询条件。" +
            "查询参数之间的关系为且。订单列表按照创建时间从新到旧排序")
    @PostMapping("/list/{pageNum}/{pageSize}")
    public Result<CommonPage<OrderListDTO>> list(@PathVariable(required = false) Integer pageNum,
                                                 @PathVariable(required = false) Integer pageSize,
                                                 @RequestBody(required = false) QueryOrderListParam param) {
        pageNum = (pageNum == null) ? 1 : pageNum;
        pageSize = (pageSize == null) ? 10 : pageSize;
        return Result.data(orderService.list(pageNum, pageSize, param));
    }

    @ApiOperation(value = "获取订单详情", notes = "获取订单详情，如果订单不存在则返回code=500")
    @GetMapping("/{id}")
    public Result<OrderDetailDTO> getOrderDetail(@PathVariable Long id) {
        try {
            return Result.data(orderService.getOrderDetail(id));
        } catch (IllegalOrderException e) {
            return Result.fail("订单不存在");
        }
    }

    @ApiOperation(value = "创建订单")
    @PostMapping("create")
    public Result<Object> createOrder(@RequestBody CreateOrderParam param) {
        try {
            orderService.createOrder(param);
            return Result.success();
        } catch (IllegalOrderException e) {
            return Result.fail(e.getResultCode(), e.getMessage());
        }
    }
}
