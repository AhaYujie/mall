package online.ahayujie.mall.admin.oms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import online.ahayujie.mall.admin.oms.bean.dto.AutoCommentSettingDTO;
import online.ahayujie.mall.admin.oms.bean.dto.OrderSettingDTO;
import online.ahayujie.mall.admin.oms.service.OrderSettingService;
import online.ahayujie.mall.common.api.Result;
import org.quartz.SchedulerException;
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

    @ApiOperation(value = "获取发货未确认收货超时自动确认cron设置")
    @GetMapping("auto-confirm-receive-cron")
    public Result<String> getAutoConfirmReceiveCron() {
        return Result.data(orderSettingService.getAutoConfirmReceiveCron());
    }

    @ApiOperation(value = "设置发货未确认收货超时自动确认cron设置")
    @PostMapping("/auto-confirm-receive-cron/update")
    public Result<Object> updateAutoConfirmReceiveCron(@RequestParam String cron) {
        try {
            orderSettingService.updateAutoConfirmReceiveCron(cron);
            return Result.success();
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        } catch (SchedulerException e) {
            return Result.fail();
        }
    }

    @ApiOperation(value = "初始化自动确认收货定时任务")
    @PostMapping("/auto-confirm-receive-job/init")
    public Result<Object> initAutoConfirmReceiveJob() {
        try {
            orderSettingService.initAutoConfirmReceiveJob();
            return Result.success();
        } catch (SchedulerException e) {
            return Result.fail();
        }
    }

    @ApiOperation(value = "暂停自动确认收货定时任务")
    @PostMapping("/auto-confirm-receive-job/pause")
    public Result<Object> pauseAutoConfirmReceiveJob() {
        try {
            orderSettingService.pauseAutoConfirmReceiveJob();
            return Result.success();
        } catch (SchedulerException e) {
            return Result.fail();
        }
    }

    @ApiOperation(value = "恢复自动确认收货定时任务")
    @PostMapping("/auto-confirm-receive-job/resume")
    public Result<Object> resumeAutoConfirmReceiveJob() {
        try {
            orderSettingService.resumeAutoConfirmReceiveJob();
            return Result.success();
        } catch (SchedulerException e) {
            return Result.fail();
        }
    }

    @ApiOperation(value = "删除自动确认收货定时任务")
    @PostMapping("/auto-confirm-receive-job/delete")
    public Result<Object> deleteAutoConfirmReceiveJob() {
        try {
            orderSettingService.deleteAutoConfirmReceiveJob();
            return Result.success();
        } catch (SchedulerException e) {
            return Result.fail();
        }
    }

    @ApiOperation(value = "获取发货后经过多少时间未确认收货则自动确认收货(单位秒)")
    @GetMapping("/auto-confirm-receive-time")
    public Result<Integer> getAutoConfirmReceiveTime() {
        return Result.data(orderSettingService.getAutoConfirmReceiveTime());
    }

    @ApiOperation(value = "更新发货后经过多少时间未确认收货则自动确认收货(单位秒)")
    @PostMapping("/auto-confirm-receive-time/update")
    public Result<Object> updateAutoConfirmReceiveTime(@RequestParam Integer time) {
        try {
            orderSettingService.updateAutoConfirmReceiveTime(time);
            return Result.success();
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "获取确认收货后未评价超时自动评价cron设置")
    @GetMapping("auto-comment-cron")
    public Result<String> getAutoCommentCron() {
        return Result.data(orderSettingService.getAutoCommentCron());
    }

    @ApiOperation(value = "设置确认收货后未评价超时自动评价cron设置")
    @PostMapping("/auto-comment-cron/update")
    public Result<Object> updateAutoCommentCron(@RequestParam String cron) {
        try {
            orderSettingService.updateAutoCommentCron(cron);
            return Result.success();
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        } catch (SchedulerException e) {
            return Result.fail();
        }
    }

    @ApiOperation(value = "初始化自动评价定时任务")
    @PostMapping("/auto-comment-job/init")
    public Result<Object> initAutoCommentJob() {
        try {
            orderSettingService.initAutoCommentJob();
            return Result.success();
        } catch (SchedulerException e) {
            return Result.fail();
        }
    }

    @ApiOperation(value = "暂停自动评价定时任务")
    @PostMapping("/auto-comment-job/pause")
    public Result<Object> pauseAutoCommentJob() {
        try {
            orderSettingService.pauseAutoCommentJob();
            return Result.success();
        } catch (SchedulerException e) {
            return Result.fail();
        }
    }

    @ApiOperation(value = "恢复自动评价定时任务")
    @PostMapping("/auto-comment-job/resume")
    public Result<Object> resumeAutoCommentJob() {
        try {
            orderSettingService.resumeAutoCommentJob();
            return Result.success();
        } catch (SchedulerException e) {
            return Result.fail();
        }
    }

    @ApiOperation(value = "删除自动评价定时任务")
    @PostMapping("/auto-comment-job/delete")
    public Result<Object> deleteAutoCommentJob() {
        try {
            orderSettingService.deleteAutoCommentJob();
            return Result.success();
        } catch (SchedulerException e) {
            return Result.fail();
        }
    }

    @ApiOperation(value = "获取确认收货后经过多少时间未评价则自动评价(单位秒)")
    @GetMapping("/auto-comment-time")
    public Result<Integer> getAutoCommentTime() {
        return Result.data(orderSettingService.getAutoCommentTime());
    }

    @ApiOperation(value = "更新确认收货后经过多少时间未评价则自动评价(单位秒)")
    @PostMapping("/auto-comment-time/update")
    public Result<Object> updateAutoCommentTime(@RequestParam Integer time) {
        try {
            orderSettingService.updateAutoCommentTime(time);
            return Result.success();
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "获取自动评价的评价设置")
    @GetMapping("auto-comment-setting")
    public Result<AutoCommentSettingDTO> getAutoCommentSetting() {
        return Result.data(orderSettingService.getAutoCommentSetting());
    }

    @ApiOperation(value = "更新自动评价的评价设置")
    @PostMapping("/auto-comment-setting/update")
    public Result<Object> updateAutoCommentSetting(@RequestBody AutoCommentSettingDTO autoCommentSettingDTO) {
        orderSettingService.updateAutoCommentSetting(autoCommentSettingDTO);
        return Result.success();
    }

    @ApiOperation(value = "获取订单交易完成后自动关闭交易，不能申请售后的cron设置")
    @GetMapping("auto-close-cron")
    public Result<String> getAutoCloseCron() {
        return Result.data(orderSettingService.getAutoCloseCron());
    }

    @ApiOperation(value = "设置订单交易完成后自动关闭交易，不能申请售后的cron")
    @PostMapping("/auto-close-cron/update")
    public Result<Object> updateAutoCloseCron(@RequestParam String cron) {
        try {
            orderSettingService.updateAutoCloseCron(cron);
            return Result.success();
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        } catch (SchedulerException e) {
            return Result.fail();
        }
    }

    @ApiOperation(value = "初始化自动关闭订单定时任务")
    @PostMapping("/auto-close-job/init")
    public Result<Object> initAutoCloseJob() {
        try {
            orderSettingService.initAutoCloseJob();
            return Result.success();
        } catch (SchedulerException e) {
            return Result.fail();
        }
    }

    @ApiOperation(value = "暂停自动关闭订单定时任务")
    @PostMapping("/auto-close-job/pause")
    public Result<Object> pauseAutoCloseJob() {
        try {
            orderSettingService.pauseAutoCloseJob();
            return Result.success();
        } catch (SchedulerException e) {
            return Result.fail();
        }
    }

    @ApiOperation(value = "恢复自动关闭订单定时任务")
    @PostMapping("/auto-close-job/resume")
    public Result<Object> resumeAutoCloseJob() {
        try {
            orderSettingService.resumeAutoCloseJob();
            return Result.success();
        } catch (SchedulerException e) {
            return Result.fail();
        }
    }

    @ApiOperation(value = "删除自动关闭订单定时任务")
    @PostMapping("/auto-close-job/delete")
    public Result<Object> deleteAutoCloseJob() {
        try {
            orderSettingService.deleteAutoCloseJob();
            return Result.success();
        } catch (SchedulerException e) {
            return Result.fail();
        }
    }

    @ApiOperation(value = "获取订单完成后经过多少时间自动关闭订单(单位秒)")
    @GetMapping("/auto-close-time")
    public Result<Integer> getAutoCloseTime() {
        return Result.data(orderSettingService.getAutoCloseTime());
    }

    @ApiOperation(value = "更新订单完成后经过多少时间自动关闭订单(单位秒)")
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
