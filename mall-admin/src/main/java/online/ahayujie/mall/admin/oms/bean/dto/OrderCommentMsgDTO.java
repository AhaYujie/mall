package online.ahayujie.mall.admin.oms.bean.dto;

import lombok.Data;

import java.util.List;

/**
 * @author aha
 * @since 2020/10/6
 */
@Data
public class OrderCommentMsgDTO {
    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 评价的订单商品
     */
    private List<OrderProductComment> orderProductComments;

    public OrderCommentMsgDTO() {
    }

    public OrderCommentMsgDTO(Long orderId, List<OrderProductComment> orderProductComments) {
        this.orderId = orderId;
        this.orderProductComments = orderProductComments;
    }

    @Data
    public static class OrderProductComment {
        /**
         * 订单商品id
         */
        private Long orderProductId;

        /**
         * 评论id
         */
        private Long commentId;

        public OrderProductComment() {
        }

        public OrderProductComment(Long orderProductId, Long commentId) {
            this.orderProductId = orderProductId;
            this.commentId = commentId;
        }
    }
}
