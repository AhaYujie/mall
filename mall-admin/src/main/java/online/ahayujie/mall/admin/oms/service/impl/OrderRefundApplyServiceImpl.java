package online.ahayujie.mall.admin.oms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.admin.oms.bean.dto.OrderRefundApplyDetailDTO;
import online.ahayujie.mall.admin.oms.bean.dto.OrderRefundApplyRefusedMsgDTO;
import online.ahayujie.mall.admin.oms.bean.dto.RefuseOrderRefundApplyParam;
import online.ahayujie.mall.admin.oms.bean.model.OrderRefundApply;
import online.ahayujie.mall.admin.oms.bean.model.OrderRefundApplyProduct;
import online.ahayujie.mall.admin.oms.exception.IllegalOrderRefundApplyException;
import online.ahayujie.mall.admin.oms.mapper.OrderRefundApplyMapper;
import online.ahayujie.mall.admin.oms.mapper.OrderRefundApplyProductMapper;
import online.ahayujie.mall.admin.oms.publisher.OrderPublisher;
import online.ahayujie.mall.admin.oms.service.OrderRefundApplyService;
import online.ahayujie.mall.admin.oms.service.OrderService;
import online.ahayujie.mall.common.api.CommonPage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    private OrderService orderService;
    private OrderPublisher orderPublisher;

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

    @Override
    public void refuseApply(RefuseOrderRefundApplyParam param) throws IllegalOrderRefundApplyException {
        OrderRefundApply apply = orderRefundApplyMapper.selectById(param.getId());
        if (apply == null) {
            throw new IllegalOrderRefundApplyException("订单仅退款申请不存在");
        }
        if (!OrderRefundApply.Status.APPLYING.getValue().equals(apply.getStatus())) {
            throw new IllegalOrderRefundApplyException("当前订单仅退款申请不支持此操作");
        }
        OrderRefundApply updateApply = new OrderRefundApply();
        updateApply.setId(param.getId());
        updateApply.setUpdateTime(new Date());
        updateApply.setHandleTime(new Date());
        updateApply.setHandleNote(param.getHandleNote());
        updateApply.setStatus(OrderRefundApply.Status.REFUSED.getValue());
        orderRefundApplyMapper.updateById(updateApply);
        Long orderId = apply.getOrderId();
        List<OrderRefundApplyProduct> products = orderRefundApplyProductMapper.selectByApplyId(param.getId());
        List<Long> ids = products.stream().map(OrderRefundApplyProduct::getOrderProductId).collect(Collectors.toList());
        orderService.refuseAfterSaleApply(orderId, ids);
        // 发送消息到消息队列
        OrderRefundApplyRefusedMsgDTO msgDTO = new OrderRefundApplyRefusedMsgDTO(orderId, param.getId(), param.getHandleNote());
        orderPublisher.publishRefundApplyRefusedMsg(msgDTO);
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setOrderPublisher(OrderPublisher orderPublisher) {
        this.orderPublisher = orderPublisher;
    }
}
