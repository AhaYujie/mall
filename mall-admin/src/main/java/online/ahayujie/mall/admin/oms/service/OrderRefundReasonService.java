package online.ahayujie.mall.admin.oms.service;

import online.ahayujie.mall.admin.oms.bean.dto.CreateOrderRefundReasonParam;
import online.ahayujie.mall.admin.oms.bean.dto.UpdateOrderRefundReasonParam;
import online.ahayujie.mall.admin.oms.bean.model.OrderRefundReason;
import online.ahayujie.mall.admin.oms.exception.IllegalOrderRefundReasonException;
import online.ahayujie.mall.common.api.CommonPage;

/**
 * <p>
 * 订单仅退款原因 服务类
 * </p>
 *
 * @author aha
 * @since 2020-08-08
 */
public interface OrderRefundReasonService {
    /**
     * 新增订单仅退款原因
     * @param param 订单仅退款原因信息
     * @throws IllegalOrderRefundReasonException 订单仅退款原因启用状态不合法
     */
    void create(CreateOrderRefundReasonParam param) throws IllegalOrderRefundReasonException;

    /**
     * 更新订单仅退款原因
     * @param param 订单仅退款原因信息
     * @param id 订单仅退款原因id
     * @throws IllegalOrderRefundReasonException 订单仅退款原因不存在或启用状态不合法
     */
    void update(UpdateOrderRefundReasonParam param, Long id) throws IllegalOrderRefundReasonException;

    /**
     * 删除订单仅退款原因。
     * 若不存在则忽略不做操作。
     * @param id 订单仅退款原因id
     */
    void delete(Long id);

    /**
     * 分页获取订单仅退款原因。
     * 根据排序字段从大到小排序。
     * @param pageNum 页索引
     * @param pageSize 页大小
     * @return 订单仅退款原因
     */
    CommonPage<OrderRefundReason> list(Integer pageNum, Integer pageSize);
}
