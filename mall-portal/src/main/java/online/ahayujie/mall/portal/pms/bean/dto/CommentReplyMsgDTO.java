package online.ahayujie.mall.portal.pms.bean.dto;

import lombok.Data;

/**
 * @author aha
 * @since 2020/10/7
 */
@Data
public class CommentReplyMsgDTO {
    /**
     * 商品评价id
     */
    private Long commentId;

    /**
     * 商品评价回复id
     */
    private Long commentReplyId;

    /**
     * 商品评价回复内容
     */
    private String content;

    public CommentReplyMsgDTO() {
    }

    public CommentReplyMsgDTO(Long commentId, Long commentReplyId, String content) {
        this.commentId = commentId;
        this.commentReplyId = commentReplyId;
        this.content = content;
    }
}
