package online.ahayujie.mall.portal.pms.publisher;

import online.ahayujie.mall.portal.config.RabbitmqConfig;
import online.ahayujie.mall.portal.pms.bean.dto.CommentReplyMsgDTO;

/**
 * 商品评价消息发送者
 * @author aha
 * @since 2020/10/7
 */
public interface CommentPublisher {
    /**
     * 发送回复商品评价消息。
     * exchange为 {@link RabbitmqConfig#PRODUCT_COMMENT_REPLY_EXCHANGE}
     *
     * @param msgDTO 回复商品评价消息
     */
    void publishCommentReplyMsg(CommentReplyMsgDTO msgDTO);
}
