package online.ahayujie.mall.admin.pms.controller;


import io.swagger.annotations.ApiOperation;
import online.ahayujie.mall.admin.pms.bean.dto.CommentReplyParam;
import online.ahayujie.mall.admin.pms.bean.model.Comment;
import online.ahayujie.mall.admin.pms.bean.model.CommentReplay;
import online.ahayujie.mall.admin.pms.service.CommentService;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.common.api.Result;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 商品评价表 前端控制器
 * </p>
 *
 * @author aha
 * @since 2020-10-06
 */
@RestController
@RequestMapping("/product-comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @ApiOperation(value = "分页获取商品评价", notes = "分页获取商品评价，默认第一页，每页20个")
    @GetMapping("list")
    public Result<CommonPage<Comment>> list(@RequestParam(required = false, defaultValue = "1") Long pageNum,
                                            @RequestParam(required = false, defaultValue = "20") Long pageSize,
                                            @RequestParam Long productId) {
        return Result.data(commentService.list(pageNum, pageSize, productId));
    }

    @ApiOperation(value = "设置商品评价是否可见", notes = "设置商品评价是否可见，0为不可见，1为可见")
    @PostMapping("isShow")
    public Result<Object> updateCommentIsShow(@RequestParam Long commentId, @RequestParam Integer isShow) {
        try {
            commentService.updateCommentIsShow(commentId, isShow);
            return Result.success();
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "分页获取商品评价回复", notes = "分页获取商品评价回复，默认第一页，每页20个")
    @GetMapping("/reply/list")
    public Result<CommonPage<CommentReplay>> replyList(@RequestParam(required = false, defaultValue = "1") Long pageNum,
                                                       @RequestParam(required = false, defaultValue = "20") Long pageSize,
                                                       @RequestParam Long commentId) {
        return Result.data(commentService.replyList(pageNum, pageSize, commentId));
    }

    @ApiOperation(value = "管理员回复商品评价")
    @PostMapping("/reply")
    public Result<Object> replyComment(@RequestBody CommentReplyParam param) {
        try {
            commentService.replyComment(param);
            return Result.success();
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }
}
