package online.ahayujie.mall.admin.oms.service;

import online.ahayujie.mall.admin.oms.bean.dto.OrderReturnApplyDetailDTO;
import online.ahayujie.mall.admin.oms.bean.dto.RefuseOrderReturnApplyParam;
import online.ahayujie.mall.admin.oms.bean.model.OrderReturnApply;
import online.ahayujie.mall.admin.oms.exception.IllegalOrderReturnApplyException;
import online.ahayujie.mall.common.api.CommonPage;

import java.util.List;

/**
 * <p>
 * 订单退货退款申请 服务类
 * </p>
 *
 * @author aha
 * @since 2020-08-08
 */
public interface OrderReturnApplyService {
    /**
     * 分页获取订单退货退款申请。
     * 按照创建时间从新到旧排序。
     *
     * @param pageNum 页索引
     * @param pageSize 页大小
     * @return 订单退货退款申请
     */
    CommonPage<OrderReturnApply> list(Long pageNum, Long pageSize);

    /**
     * 分页查询订单退货退款申请
     * 查询条件之间的关系为且。
     * 按照创建时间从新到旧排序。
     *
     * @param pageNum 页索引
     * @param pageSize 页大小
     * @param orderSn 订单号
     * @param status 申请状态
     * @return 订单退货退款申请
     */
    CommonPage<OrderReturnApply> query(Long pageNum, Long pageSize, String orderSn, Integer status);

    /**
     * 查询某一订单的退货退款申请
     * @param orderId 订单id
     * @return 订单的退货退款申请
     */
    List<OrderReturnApply> queryByOrderId(Long orderId);

    /**
     * 获取退货退款申请详情。
     * 不存在则返回null。
     *
     * @param id 退货退款申请id
     * @return 退货退款申请详情
     */
    OrderReturnApplyDetailDTO getDetailById(Long id);

    /**
     * 拒绝订单退货退款申请。
     * 操作完成后发送消息到消息队列。
     *
     * @param param 请求参数
     * @throws IllegalOrderReturnApplyException 申请不存在或当前申请不支持此操作
     */
    void refuseApply(RefuseOrderReturnApplyParam param) throws IllegalOrderReturnApplyException;
}
