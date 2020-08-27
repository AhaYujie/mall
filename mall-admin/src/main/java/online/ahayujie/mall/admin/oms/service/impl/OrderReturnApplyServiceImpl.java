package online.ahayujie.mall.admin.oms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.admin.oms.bean.dto.OrderReturnApplyDetailDTO;
import online.ahayujie.mall.admin.oms.bean.model.OrderReturnApply;
import online.ahayujie.mall.admin.oms.bean.model.OrderReturnApplyProduct;
import online.ahayujie.mall.admin.oms.mapper.OrderReturnApplyMapper;
import online.ahayujie.mall.admin.oms.mapper.OrderReturnApplyProductMapper;
import online.ahayujie.mall.admin.oms.service.OrderReturnApplyService;
import online.ahayujie.mall.common.api.CommonPage;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 订单退货退款申请 服务实现类
 * </p>
 *
 * @author aha
 * @since 2020-08-08
 */
@Service
public class OrderReturnApplyServiceImpl implements OrderReturnApplyService {
    private final OrderReturnApplyMapper orderReturnApplyMapper;
    private final OrderReturnApplyProductMapper orderReturnApplyProductMapper;

    public OrderReturnApplyServiceImpl(OrderReturnApplyMapper orderReturnApplyMapper,
                                       OrderReturnApplyProductMapper orderReturnApplyProductMapper) {
        this.orderReturnApplyMapper = orderReturnApplyMapper;
        this.orderReturnApplyProductMapper = orderReturnApplyProductMapper;
    }

    @Override
    public CommonPage<OrderReturnApply> list(Long pageNum, Long pageSize) {
        Page<OrderReturnApply> page = new Page<>(pageNum, pageSize);
        IPage<OrderReturnApply> orderReturnApplyPage = orderReturnApplyMapper.selectByPage(page);
        return new CommonPage<>(orderReturnApplyPage);
    }

    @Override
    public CommonPage<OrderReturnApply> query(Long pageNum, Long pageSize, String orderSn, Integer status) {
        Page<OrderReturnApply> page = new Page<>(pageNum, pageSize);
        IPage<OrderReturnApply> orderReturnApplyPage = orderReturnApplyMapper.queryByPage(page, orderSn, status);
        return new CommonPage<>(orderReturnApplyPage);
    }

    @Override
    public List<OrderReturnApply> queryByOrderId(Long orderId) {
        return orderReturnApplyMapper.selectByOrderId(orderId);
    }

    @Override
    public OrderReturnApplyDetailDTO getDetailById(Long id) {
        OrderReturnApply orderReturnApply = orderReturnApplyMapper.selectById(id);
        if (orderReturnApply == null) {
            return null;
        }
        List<OrderReturnApplyProduct> products = orderReturnApplyProductMapper.selectByApplyId(id);
        OrderReturnApplyDetailDTO detailDTO = new OrderReturnApplyDetailDTO();
        BeanUtils.copyProperties(orderReturnApply, detailDTO);
        detailDTO.setProducts(products);
        return detailDTO;
    }
}
