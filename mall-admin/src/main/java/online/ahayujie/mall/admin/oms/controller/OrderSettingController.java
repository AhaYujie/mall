package online.ahayujie.mall.admin.oms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import online.ahayujie.mall.admin.oms.bean.dto.OrderSettingDTO;
import online.ahayujie.mall.admin.oms.service.OrderSettingService;
import online.ahayujie.mall.common.api.Result;
import org.springframework.web.bind.annotation.*;

/**
 * 订单设置控制器
 * @author aha
 * @since 2020/8/12
 */
@RestController
@RequestMapping("/order-setting")
@Api(tags = "订单模块", value = "订单设置管理")
public class OrderSettingController {
    private final OrderSettingService orderSettingService;

    public OrderSettingController(OrderSettingService orderSettingService) {
        this.orderSettingService = orderSettingService;
    }

    @ApiOperation(value = "获取订单未支付超时关闭的时间，单位分钟")
    @GetMapping("un-pay-timeout")
    public Result<Integer> getUnPayTimeOut() {
        return Result.data(orderSettingService.getUnPayTimeout());
    }

    @ApiOperation(value = "设置订单未支付超时关闭的时间，单位分钟")
    @PostMapping("/un-pay-timeout/update")
    public Result<Object> updateUnPayTimeOut(@RequestParam Integer time) {
        try {
            orderSettingService.updateUnPayTimeOut(time);
            return Result.success();
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "获取发货未确认收货超时自动确认时间(单位天)")
    @GetMapping("auto-confirm-receive-time")
    public Result<Integer> getAutoConfirmReceiveTime() {
        return Result.data(orderSettingService.getAutoConfirmReceiveTime());
    }

    @ApiOperation(value = "设置发货未确认收货超时自动确认时间(单位天)")
    @PostMapping("/auto-confirm-receive-time/update")
    public Result<Object> updateAutoConfirmReceiveTime(@RequestParam Integer time) {
        try {
            orderSettingService.updateAutoConfirmReceiveTime(time);
            return Result.success();
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "获取确认收货后未评价超时自动评价时间(单位天)")
    @GetMapping("auto-comment-time")
    public Result<Integer> getAutoCommentTime() {
        return Result.data(orderSettingService.getAutoCommentTime());
    }

    @ApiOperation(value = "设置确认收货后未评价超时自动评价时间(单位天)")
    @PostMapping("/auto-comment-time/update")
    public Result<Object> updateAutoCommentTime(@RequestParam Integer time) {
        try {
            orderSettingService.updateAutoCommentTime(time);
            return Result.success();
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "获取订单交易完成后自动关闭交易，不能申请售后的时间(单位天)")
    @GetMapping("auto-close-time")
    public Result<Integer> getAutoCloseTime() {
        return Result.data(orderSettingService.getAutoCloseTime());
    }

    @ApiOperation(value = "设置订单交易完成后自动关闭交易，不能申请售后的时间(单位天)")
    @PostMapping("/auto-close-time/update")
    public Result<Object> updateAutoCloseTime(@RequestParam Integer time) {
        try {
            orderSettingService.updateAutoCloseTime(time);
            return Result.success();
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "获取全部订单设置")
    @GetMapping("all")
    public Result<OrderSettingDTO> getAll() {
        return Result.data(orderSettingService.getAll());
    }
}
