package online.ahayujie.mall.admin.pms.service;

import online.ahayujie.mall.admin.pms.bean.model.Comment;

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
}
