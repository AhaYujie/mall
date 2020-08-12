package online.ahayujie.mall.admin.oms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.admin.oms.bean.dto.CreateOrderRefundReasonParam;
import online.ahayujie.mall.admin.oms.bean.dto.UpdateOrderRefundReasonParam;
import online.ahayujie.mall.admin.oms.bean.model.OrderRefundReason;
import online.ahayujie.mall.admin.oms.exception.IllegalOrderRefundReasonException;
import online.ahayujie.mall.admin.oms.mapper.OrderRefundReasonMapper;
import online.ahayujie.mall.admin.oms.service.OrderRefundReasonService;
import online.ahayujie.mall.common.api.CommonPage;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 订单仅退款原因 服务实现类
 * </p>
 *
 * @author aha
 * @since 2020-08-08
 */
@Service
public class OrderRefundReasonServiceImpl implements OrderRefundReasonService {
    private final OrderRefundReasonMapper orderRefundReasonMapper;

    public OrderRefundReasonServiceImpl(OrderRefundReasonMapper orderRefundReasonMapper) {
        this.orderRefundReasonMapper = orderRefundReasonMapper;
    }

    @Override
    public void create(CreateOrderRefundReasonParam param) throws IllegalOrderRefundReasonException {
        OrderRefundReason orderRefundReason = new OrderRefundReason();
        BeanUtils.copyProperties(param, orderRefundReason);
        validate(orderRefundReason);
        orderRefundReason.setCreateTime(new Date());
        orderRefundReasonMapper.insert(orderRefundReason);
    }

    @Override
    public void update(UpdateOrderRefundReasonParam param, Long id) throws IllegalOrderRefundReasonException {
        OrderRefundReason old = orderRefundReasonMapper.selectById(id);
        if (old == null) {
            throw new IllegalOrderRefundReasonException("订单仅退款原因不存在");
        }
        OrderRefundReason orderRefundReason = new OrderRefundReason();
        BeanUtils.copyProperties(param, orderRefundReason);
        validate(orderRefundReason);
        orderRefundReason.setId(id);
        orderRefundReason.setUpdateTime(new Date());
        orderRefundReasonMapper.updateById(orderRefundReason);
    }

    @Override
    public void delete(Long id) {
        orderRefundReasonMapper.deleteById(id);
    }

    @Override
    public CommonPage<OrderRefundReason> list(Integer pageNum, Integer pageSize) {
        Page<OrderRefundReason> page = new Page<>(pageNum, pageSize);
        IPage<OrderRefundReason> orderRefundReasonPage = orderRefundReasonMapper.selectByPage(page);
        return new CommonPage<>(orderRefundReasonPage);
    }

    /**
     * 检查订单仅退款原因合法性。
     * 若某一字段为null则忽略。
     * @param orderRefundReason 订单仅退款原因
     * @throws IllegalOrderRefundReasonException 订单仅退款原因不合法
     */
    private void validate(OrderRefundReason orderRefundReason) throws IllegalOrderRefundReasonException {
        Integer status = orderRefundReason.getStatus();
        if (status != null) {
            if (status != OrderRefundReason.NOT_ACTIVE_STATUS && status != OrderRefundReason.ACTIVE_STATUS) {
                throw new IllegalOrderRefundReasonException("订单仅退款原因启用状态不合法");
            }
        }
    }
}
