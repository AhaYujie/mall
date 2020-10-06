package online.ahayujie.mall.admin.oms.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.mms.bean.model.Member;
import online.ahayujie.mall.admin.mms.service.MemberService;
import online.ahayujie.mall.admin.oms.bean.dto.OrderCommentMsgDTO;
import online.ahayujie.mall.admin.oms.bean.model.Order;
import online.ahayujie.mall.admin.oms.bean.model.OrderProduct;
import online.ahayujie.mall.admin.oms.mapper.OrderMapper;
import online.ahayujie.mall.admin.oms.mapper.OrderProductMapper;
import online.ahayujie.mall.admin.oms.publisher.OrderPublisher;
import online.ahayujie.mall.admin.oms.service.AbstractOrderState;
import online.ahayujie.mall.admin.oms.service.OrderContext;
import online.ahayujie.mall.admin.pms.bean.model.Comment;
import online.ahayujie.mall.admin.pms.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 待评价订单状态 Service
 * @author aha
 * @since 2020/8/16
 */
@Slf4j
@Service(value = Order.UN_COMMENT_STATUS_NAME)
public class UnCommentOrderState extends AbstractOrderState {
    private MemberService memberService;
    private CommentService commentService;

    private final OrderMapper orderMapper;
    private final OrderPublisher orderPublisher;
    private final OrderProductMapper orderProductMapper;

    public UnCommentOrderState(ApplicationContext applicationContext, OrderMapper orderMapper, OrderPublisher orderPublisher,
                               OrderProductMapper orderProductMapper) {
        super(applicationContext);
        this.orderMapper = orderMapper;
        this.orderPublisher = orderPublisher;
        this.orderProductMapper = orderProductMapper;
    }

    /**
     * 评价订单。
     * 操作成功后发送消息到消息队列。
     * 操作成功后如果订单商品全部评价完成则订单状态变为 {@link Order.Status#COMPLETE}。
     *
     * @param orderContext orderContext
     * @param orderId 订单id
     * @param orderProductIds 订单商品id
     * @param content 评价内容
     * @param pics 评价图片
     * @param star 评价星数
     * @throws UnsupportedOperationException 当前订单状态不支持此操作
     */
    @Override
    public void comment(OrderContext orderContext, Long orderId, List<Long> orderProductIds, String content,
                        String pics, Integer star) throws UnsupportedOperationException {
        Order order = orderMapper.selectById(orderId);
        List<OrderCommentMsgDTO.OrderProductComment> orderProductComments = new ArrayList<>();
        for (Long orderProductId : orderProductIds) {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setId(orderProductId);
            orderProduct.setIsComment(OrderProduct.COMMENT);
            orderProduct.setUpdateTime(new Date());
            orderProductMapper.updateById(orderProduct);
            orderProduct = orderProductMapper.selectById(orderProductId);
            Member member = memberService.getById(order.getMemberId());
            Comment comment = new Comment();
            comment.setProductId(orderProduct.getProductId());
            comment.setMemberId(member.getId());
            comment.setMemberNickname(member.getNickname());
            comment.setMemberIcon(member.getIcon());
            comment.setProductName(orderProduct.getProductName());
            comment.setSpecification(orderProduct.getSpecification());
            comment.setContent(content);
            comment.setPics(pics);
            comment.setStar(star);
            comment = commentService.saveComment(comment);
            orderProductComments.add(new OrderCommentMsgDTO.OrderProductComment(orderProductId, comment.getId()));
        }
        boolean isAll = true;
        List<OrderProduct> orderProducts = orderProductMapper.selectByOrderId(orderId);
        for (OrderProduct orderProduct : orderProducts) {
            isAll = (isAll && orderProduct.getIsComment() == OrderProduct.COMMENT);
        }
        if (isAll) {
            order = new Order();
            order.setId(orderId);
            order.setUpdateTime(new Date());
            order.setStatus(Order.Status.COMPLETE.getValue());
            orderMapper.updateById(order);
            orderContext.setOrderState(getOrderState(Order.Status.COMPLETE));
        }
        orderPublisher.publishCommentMsg(new OrderCommentMsgDTO(orderId, orderProductComments));
    }

    @Autowired
    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }

    @Autowired
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }
}
