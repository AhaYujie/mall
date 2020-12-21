package online.ahayujie.mall.portal.oms.controller;

import io.swagger.annotations.ApiOperation;
import online.ahayujie.mall.common.api.Result;
import online.ahayujie.mall.portal.oms.bean.dto.ConfirmOrderDTO;
import online.ahayujie.mall.portal.oms.bean.dto.GenerateConfirmOrderParam;
import online.ahayujie.mall.portal.oms.bean.dto.SubmitOrderDTO;
import online.ahayujie.mall.portal.oms.bean.dto.SubmitOrderParam;
import online.ahayujie.mall.portal.oms.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author aha
 * @since 2020-11-07
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @ApiOperation(value = "生成确认订单信息", notes = "此阶段不对商品库存进行校验")
    @PostMapping("generate-confirm-order")
    public Result<ConfirmOrderDTO> generateConfirmOrder(@RequestBody GenerateConfirmOrderParam param) {
        try {
            return Result.data(orderService.generateConfirmOrder(param));
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "提交订单", notes = "提交订单，扣减商品库存成功则生成订单")
    @PostMapping("submit")
    public Result<SubmitOrderDTO> submit(@RequestBody SubmitOrderParam param) {
        try {
            return Result.data(orderService.submit(param));
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }
}
