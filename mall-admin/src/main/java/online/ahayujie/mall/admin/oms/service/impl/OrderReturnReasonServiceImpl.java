package online.ahayujie.mall.admin.oms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.admin.oms.bean.dto.CreateOrderReturnReasonParam;
import online.ahayujie.mall.admin.oms.bean.dto.UpdateOrderReturnReasonParam;
import online.ahayujie.mall.admin.oms.bean.model.OrderReturnReason;
import online.ahayujie.mall.admin.oms.exception.IllegalOrderReturnReasonException;
import online.ahayujie.mall.admin.oms.mapper.OrderReturnReasonMapper;
import online.ahayujie.mall.admin.oms.service.OrderReturnReasonService;
import online.ahayujie.mall.common.api.CommonPage;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 退货原因表 服务实现类
 * </p>
 *
 * @author aha
 * @since 2020-08-08
 */
@Service
public class OrderReturnReasonServiceImpl implements OrderReturnReasonService {
    private final OrderReturnReasonMapper orderReturnReasonMapper;

    public OrderReturnReasonServiceImpl(OrderReturnReasonMapper orderReturnReasonMapper) {
        this.orderReturnReasonMapper = orderReturnReasonMapper;
    }

    @Override
    public void create(CreateOrderReturnReasonParam param) throws IllegalOrderReturnReasonException {
        OrderReturnReason orderReturnReason = new OrderReturnReason();
        BeanUtils.copyProperties(param, orderReturnReason);
        validate(orderReturnReason);
        orderReturnReason.setCreateTime(new Date());
        orderReturnReasonMapper.insert(orderReturnReason);
    }

    @Override
    public void update(Long id, UpdateOrderReturnReasonParam param) throws IllegalOrderReturnReasonException {
        OrderReturnReason old = orderReturnReasonMapper.selectById(id);
        if (old == null) {
            throw new IllegalOrderReturnReasonException("订单退货退款原因不存在");
        }
        OrderReturnReason orderReturnReason = new OrderReturnReason();
        BeanUtils.copyProperties(param, orderReturnReason);
        validate(orderReturnReason);
        orderReturnReason.setId(id);
        orderReturnReason.setUpdateTime(new Date());
        orderReturnReasonMapper.updateById(orderReturnReason);
    }

    @Override
    public void delete(Long id) {
        orderReturnReasonMapper.deleteById(id);
    }

    @Override
    public CommonPage<OrderReturnReason> list(Integer pageNum, Integer pageSize) {
        Page<OrderReturnReason> page = new Page<>(pageNum, pageSize);
        IPage<OrderReturnReason> orderReturnReasonPage = orderReturnReasonMapper.selectByPage(page);
        return new CommonPage<>(orderReturnReasonPage);
    }

    /**
     * 检查订单退货退款信息合法性。
     * 若某一字段为null则忽略不检查。
     * @param orderReturnReason 订单退货退款信息
     * @throws IllegalOrderReturnReasonException 订单退货退款信息不合法
     */
    private void validate(OrderReturnReason orderReturnReason) throws IllegalOrderReturnReasonException {
        Integer status = orderReturnReason.getStatus();
        if (status != null) {
            if (status != OrderReturnReason.NOT_ACTIVE_STATUS && status != OrderReturnReason.ACTIVE_STATUS) {
                throw new IllegalOrderReturnReasonException("订单退货退款启用状态不合法");
            }
        }
    }
}
