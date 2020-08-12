package online.ahayujie.mall.admin.oms.service;

import online.ahayujie.mall.admin.oms.bean.dto.CreateOrderReturnReasonParam;
import online.ahayujie.mall.admin.oms.bean.dto.UpdateOrderReturnReasonParam;
import online.ahayujie.mall.admin.oms.bean.model.OrderReturnReason;
import online.ahayujie.mall.admin.oms.exception.IllegalOrderReturnReasonException;
import online.ahayujie.mall.common.api.CommonPage;

/**
 * <p>
 * 退货原因表 服务类
 * </p>
 *
 * @author aha
 * @since 2020-08-08
 */
public interface OrderReturnReasonService {
    /**
     * 新增订单退货退款原因
     * @param param 订单退货退款原因信息
     * @throws IllegalOrderReturnReasonException 订单退货退款原因的启用状态不合法
     */
    void create(CreateOrderReturnReasonParam param) throws IllegalOrderReturnReasonException;

    /**
     * 更新订单退货退款原因
     * @param id 订单退货退款原因id
     * @param param 订单退货退款原因信息
     * @throws IllegalOrderReturnReasonException 订单退货退款原因不存在或不合法
     */
    void update(Long id, UpdateOrderReturnReasonParam param) throws IllegalOrderReturnReasonException;

    /**
     * 删除订单退货退款原因。
     * 若订单退货退款原因不存在则忽略。
     * @param id 订单退货退款原因id
     */
    void delete(Long id);

    /**
     * 分页获取订单退货退款原因。
     * 根据排序字段从大到小排序。
     * @param pageNum 页索引
     * @param pageSize 页大小
     * @return 订单退货退款原因
     */
    CommonPage<OrderReturnReason> list(Integer pageNum, Integer pageSize);
}
