package online.ahayujie.mall.admin.pms.service;

import online.ahayujie.mall.admin.pms.bean.dto.CommentReplyParam;
import online.ahayujie.mall.admin.pms.bean.model.Comment;
import online.ahayujie.mall.admin.pms.bean.model.CommentReplay;
import online.ahayujie.mall.common.api.CommonPage;

/**
 * <p>
 * 商品评价表 服务类
 * </p>
 *
 * @author aha
 * @since 2020-10-06
 */
public interface CommentService {
    /**
     * 保存评论。
     * @param comment 评论
     * @return 保存后的评论
     */
    Comment saveComment(Comment comment);

    /**
     * 分页获取商品评价。
     *
     * @param pageNum 页索引
     * @param pageSize 页大小
     * @param productId 商品id
     * @return 商品评价
     */
    CommonPage<Comment> list(Long pageNum, Long pageSize, Long productId);

    /**
     * 设置商品评价是否可见
     * @param commentId 商品评价id
     * @param isShow 是否可见
     * @throws IllegalArgumentException isShow参数不合法
     */
    void updateCommentIsShow(Long commentId, Integer isShow) throws IllegalArgumentException;

    /**
     * 分页获取商品评价回复
     * @param pageNum 页索引
     * @param pageSize 页大小
     * @param commentId 商品评价id
     * @return 商品评价回复
     */
    CommonPage<CommentReplay> replyList(Long pageNum, Long pageSize, Long commentId);

    /**
     * 管理员回复商品评价。
     * 操作成功则增加商品评价的replyCount
     * 操作成功发送消息到消息队列。
     *
     * @param param 商品评价回复参数
     * @throws IllegalArgumentException 商品评价不存在
     */
    void replyComment(CommentReplyParam param) throws IllegalArgumentException;
}
