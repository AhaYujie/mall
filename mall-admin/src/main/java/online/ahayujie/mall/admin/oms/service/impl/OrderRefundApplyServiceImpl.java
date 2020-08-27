package online.ahayujie.mall.admin.oms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.admin.oms.bean.dto.OrderRefundApplyDetailDTO;
import online.ahayujie.mall.admin.oms.bean.model.OrderRefundApply;
import online.ahayujie.mall.admin.oms.bean.model.OrderRefundApplyProduct;
import online.ahayujie.mall.admin.oms.mapper.OrderRefundApplyMapper;
import online.ahayujie.mall.admin.oms.mapper.OrderRefundApplyProductMapper;
import online.ahayujie.mall.admin.oms.service.OrderRefundApplyService;
import online.ahayujie.mall.common.api.CommonPage;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 订单仅退款申请 服务实现类
 * </p>
 *
 * @author aha
 * @since 2020-08-08
 */
@Service
public class OrderRefundApplyServiceImpl implements OrderRefundApplyService {
    private final OrderRefundApplyMapper orderRefundApplyMapper;
    private final OrderRefundApplyProductMapper orderRefundApplyProductMapper;

    public OrderRefundApplyServiceImpl(OrderRefundApplyMapper orderRefundApplyMapper,
                                       OrderRefundApplyProductMapper orderRefundApplyProductMapper) {
        this.orderRefundApplyMapper = orderRefundApplyMapper;
        this.orderRefundApplyProductMapper = orderRefundApplyProductMapper;
    }

    @Override
    public CommonPage<OrderRefundApply> list(Long pageNum, Long pageSize) {
        Page<OrderRefundApply> page = new Page<>(pageNum, pageSize);
        IPage<OrderRefundApply> orderRefundApplyPage = orderRefundApplyMapper.selectByPage(page);
        return new CommonPage<>(orderRefundApplyPage);
    }

    @Override
    public CommonPage<OrderRefundApply> query(Long pageNum, Long pageSize, String orderSn, Integer status) {
        Page<OrderRefundApply> page = new Page<>(pageNum, pageSize);
        IPage<OrderRefundApply> orderRefundApplyPage = orderRefundApplyMapper.queryByPage(page, orderSn, status);
        return new CommonPage<>(orderRefundApplyPage);
    }

    @Override
    public List<OrderRefundApply> queryByOrderId(Long orderId) {
        return orderRefundApplyMapper.queryByOrderId(orderId);
    }

    @Override
    public OrderRefundApplyDetailDTO getDetailById(Long id) {
        OrderRefundApply orderRefundApply = orderRefundApplyMapper.selectById(id);
        if (orderRefundApply == null) {
            return null;
        }
        List<OrderRefundApplyProduct> products = orderRefundApplyProductMapper.selectByApplyId(id);
        OrderRefundApplyDetailDTO detailDTO = new OrderRefundApplyDetailDTO();
        BeanUtils.copyProperties(orderRefundApply, detailDTO);
        detailDTO.setProducts(products);
        return detailDTO;
    }
}
