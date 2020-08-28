package online.ahayujie.mall.admin.oms.service;

import online.ahayujie.mall.admin.oms.bean.dto.OrderRefundApplyDetailDTO;
import online.ahayujie.mall.admin.oms.bean.dto.OrderRefundApplyRefusedMsgDTO;
import online.ahayujie.mall.admin.oms.bean.dto.RefuseOrderRefundApplyParam;
import online.ahayujie.mall.admin.oms.bean.model.OrderRefundApply;
import online.ahayujie.mall.admin.oms.exception.IllegalOrderRefundApplyException;
import online.ahayujie.mall.admin.oms.publisher.OrderPublisher;
import online.ahayujie.mall.common.api.CommonPage;

import java.util.List;

/**
 * <p>
 * 订单仅退款申请 服务类
 * </p>
 *
 * @author aha
 * @since 2020-08-08
 */
public interface OrderRefundApplyService {
    /**
     * 分页获取订单仅退款申请。
     * 按照创建时间从新到旧排序。
     *
     * @param pageNum 页索引
     * @param pageSize 页大小
     * @return 订单仅退款申请
     */
    CommonPage<OrderRefundApply> list(Long pageNum, Long pageSize);

    /**
     * 分页查询订单仅退款申请。
     * 查询条件之间的关系为且。
     * 按照创建时间从新到旧排序。
     *
     * @param pageNum 页索引
     * @param pageSize 页大小
     * @param orderSn 订单号
     * @param status 申请状态
     * @return 订单仅退款申请
     */
    CommonPage<OrderRefundApply> query(Long pageNum, Long pageSize, String orderSn, Integer status);

    /**
     * 查询某一订单的仅退款申请
     * @param orderId 订单id
     * @return 订单的仅退款申请
     */
    List<OrderRefundApply> queryByOrderId(Long orderId);

    /**
     * 获取仅退款申请详情。
     * 不存在则返回null。
     *
     * @param id 仅退款申请id
     * @return 仅退款申请详情
     */
    OrderRefundApplyDetailDTO getDetailById(Long id);

    /**
     * 拒绝订单仅退款申请。
     * 处理完成后发送消息到消息队列。
     *
     * @see OrderPublisher#publishRefundApplyRefusedMsg(OrderRefundApplyRefusedMsgDTO)
     * @param param 请求参数
     * @throws IllegalOrderRefundApplyException 订单仅退款申请不存在或当前订单仅退款申请不支持此操作
     */
    void refuseApply(RefuseOrderRefundApplyParam param) throws IllegalOrderRefundApplyException;
}
