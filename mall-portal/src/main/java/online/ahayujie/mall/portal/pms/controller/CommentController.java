package online.ahayujie.mall.portal.pms.controller;


import io.swagger.annotations.ApiOperation;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.common.api.Result;
import online.ahayujie.mall.portal.pms.bean.dto.CommentDTO;
import online.ahayujie.mall.portal.pms.bean.dto.CommentReplayDTO;
import online.ahayujie.mall.portal.pms.bean.dto.CommentReplayParam;
import online.ahayujie.mall.portal.pms.service.CommentService;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 商品评价表 前端控制器
 * </p>
 *
 * @author aha
 * @since 2020-10-19
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @ApiOperation(value = "分页获取商品评价", notes = "category：0->全部评价，1->好评，2->中评，3->差评，4->有图；" +
            "sort：0->按照点赞数从大到小排序，1->按照评价时间从新到旧排序")
    @GetMapping("list")
    public Result<CommonPage<CommentDTO>> list(@RequestParam Long productId,
                                               @RequestParam(required = false, defaultValue = "1") Long pageNum,
                                               @RequestParam(required = false, defaultValue = "20") Long pageSize,
                                               @RequestParam(required = false, defaultValue = "0") Integer category,
                                               @RequestParam(required = false, defaultValue = "0") Integer sort) {
        try {
            return Result.data(commentService.list(productId, pageNum, pageSize, category, sort));
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "分页获取商品评价回复")
    @GetMapping("/reply/list")
    public Result<CommonPage<CommentReplayDTO>> replyList(@RequestParam Long commentId,
                                                          @RequestParam(required = false, defaultValue = "1") Long pageNum,
                                                          @RequestParam(required = false, defaultValue = "20") Long pageSize) {
        return Result.data(commentService.replyList(commentId, pageNum, pageSize));
    }

    @ApiOperation(value = "获取商品评价数量", notes = "category：0->全部评价，1->好评，2->中评，3->差评，4->有图")
    @GetMapping("count")
    public Result<Long> getCount(@RequestParam Long productId,
                                 @RequestParam(required = false, defaultValue = "0") Integer category) {
        try {
            return Result.data(commentService.getCount(productId, category));
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "回复商品评价")
    @PostMapping("reply")
    public Result<Object> reply(@RequestBody CommentReplayParam param) {
        try {
            commentService.reply(param);
            return Result.success();
        } catch (IllegalArgumentException e) {
            return Result.fail("商品评价不存在");
        }
    }
}
