package online.ahayujie.mall.admin.oms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import online.ahayujie.mall.admin.oms.bean.dto.AutoCommentSettingDTO;
import online.ahayujie.mall.admin.oms.bean.dto.OrderSettingDTO;
import online.ahayujie.mall.admin.oms.bean.dto.OrderTimedJobStatusDTO;
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
@Api(tags = "订单模块-订单设置管理")
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

    @ApiOperation(value = "获取订单定时任务状态")
    @GetMapping("/timed-job/status")
    public Result<OrderTimedJobStatusDTO> getTimedJobStatus(@ApiParam("0->自动确认收货; 1->自动评价; 2->自动关闭订单")
                                                            @RequestParam Integer job) {
        try {
            return Result.data(orderSettingService.getTimedJobStatus(job));
        } catch (SchedulerException e) {
            return Result.fail();
        }
    }

    @ApiOperation(value = "初始化订单定时任务")
    @PostMapping("/timed-job/init")
    public Result<Object> initTimedJob(@ApiParam("0->自动确认收货; 1->自动评价; 2->自动关闭订单") @RequestParam Integer job) {
        try {
            orderSettingService.initTimedJob(job);
            return Result.success();
        } catch (SchedulerException e) {
            return Result.fail();
        }
    }

    @ApiOperation(value = "暂停订单定时任务")
    @PostMapping("/timed-job/pause")
    public Result<Object> pauseTimedJob(@ApiParam("0->自动确认收货; 1->自动评价; 2->自动关闭订单") @RequestParam Integer job) {
        try {
            orderSettingService.pauseTimedJob(job);
            return Result.success();
        } catch (SchedulerException e) {
            return Result.fail();
        }
    }

    @ApiOperation(value = "恢复订单定时任务")
    @PostMapping("/timed-job/resume")
    public Result<Object> resumeTimedJob(@ApiParam("0->自动确认收货; 1->自动评价; 2->自动关闭订单") @RequestParam Integer job) {
        try {
            orderSettingService.resumeTimedJob(job);
            return Result.success();
        } catch (SchedulerException e) {
            return Result.fail();
        }
    }

    @ApiOperation(value = "删除订单定时任务")
    @PostMapping("/timed-job/delete")
    public Result<Object> deleteTimedJob(@ApiParam("0->自动确认收货; 1->自动评价; 2->自动关闭订单") @RequestParam Integer job) {
        try {
            orderSettingService.deleteTimedJob(job);
            return Result.success();
        } catch (SchedulerException e) {
            return Result.fail();
        }
    }

    @ApiOperation(value = "获取订单定时任务的cron")
    @GetMapping("/timed-job/cron")
    public Result<String> getTimedJobCron(@ApiParam("0->自动确认收货; 1->自动评价; 2->自动关闭订单") @RequestParam Integer job) {
        return Result.data(orderSettingService.getTimedJobCron(job));
    }

    @ApiOperation(value = "更新定时任务的cron")
    @PostMapping("/timed-job/cron/update")
    public Result<Object> updateTimedJobCron(@ApiParam("0->自动确认收货; 1->自动评价; 2->自动关闭订单") @RequestParam Integer job,
                                             @RequestParam String cron) {
        try {
            orderSettingService.updateTimedJobCron(job, cron);
            return Result.success();
        } catch (IllegalArgumentException e) {
            return Result.fail("cron表达式不合法");
        } catch (SchedulerException e) {
            return Result.fail();
        }
    }
}
