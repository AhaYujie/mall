package online.ahayujie.mall.admin.oms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.admin.oms.bean.dto.*;
import online.ahayujie.mall.admin.oms.bean.model.CompanyAddress;
import online.ahayujie.mall.admin.oms.bean.model.OrderReturnApply;
import online.ahayujie.mall.admin.oms.bean.model.OrderReturnApplyProduct;
import online.ahayujie.mall.admin.oms.exception.IllegalCompanyAddressException;
import online.ahayujie.mall.admin.oms.exception.IllegalOrderRefundApplyException;
import online.ahayujie.mall.admin.oms.exception.IllegalOrderReturnApplyException;
import online.ahayujie.mall.admin.oms.mapper.OrderReturnApplyMapper;
import online.ahayujie.mall.admin.oms.mapper.OrderReturnApplyProductMapper;
import online.ahayujie.mall.admin.oms.publisher.OrderPublisher;
import online.ahayujie.mall.admin.oms.service.CompanyAddressService;
import online.ahayujie.mall.admin.oms.service.OrderReturnApplyService;
import online.ahayujie.mall.admin.oms.service.OrderService;
import online.ahayujie.mall.common.api.CommonPage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    private OrderService orderService;
    private OrderPublisher orderPublisher;
    private CompanyAddressService companyAddressService;

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

    @Override
    public void refuseApply(RefuseOrderReturnApplyParam param) throws IllegalOrderReturnApplyException {
        OrderReturnApply apply = orderReturnApplyMapper.selectById(param.getId());
        if (apply == null) {
            throw new IllegalOrderReturnApplyException("订单退货退款申请不存在");
        }
        if (!OrderReturnApply.Status.APPLYING.getValue().equals(apply.getStatus())) {
            throw new IllegalOrderReturnApplyException("当前订单退货退款申请不支持此操作");
        }
        OrderReturnApply updateApply = new OrderReturnApply();
        updateApply.setId(param.getId());
        updateApply.setUpdateTime(new Date());
        updateApply.setHandleTime(new Date());
        updateApply.setHandleNote(param.getHandleNote());
        updateApply.setStatus(OrderReturnApply.Status.REFUSED.getValue());
        orderReturnApplyMapper.updateById(updateApply);
        Long orderId = apply.getOrderId();
        List<OrderReturnApplyProduct> products = orderReturnApplyProductMapper.selectByApplyId(param.getId());
        List<Long> ids = products.stream().map(OrderReturnApplyProduct::getOrderProductId).collect(Collectors.toList());
        orderService.refuseAfterSaleApply(orderId, ids);
        // 发送消息到消息队列
        OrderReturnApplyRefusedMsgDTO msgDTO = new OrderReturnApplyRefusedMsgDTO(orderId, param.getId(), param.getHandleNote());
        orderPublisher.publishReturnApplyRefusedMsg(msgDTO);
    }

    @Override
    public void agreeApply(Long id) throws IllegalOrderReturnApplyException {
        OrderReturnApply apply = orderReturnApplyMapper.selectById(id);
        if (apply == null) {
            throw new IllegalOrderReturnApplyException("订单退货退款申请不存在");
        }
        if (!OrderReturnApply.Status.APPLYING.getValue().equals(apply.getStatus())) {
            throw new IllegalOrderReturnApplyException("订单退货退款申请不支持此操作");
        }
        OrderReturnApply updateApply = new OrderReturnApply();
        updateApply.setId(id);
        updateApply.setUpdateTime(new Date());
        updateApply.setStatus(OrderReturnApply.Status.PROCESSING.getValue());
        orderReturnApplyMapper.updateById(updateApply);
        orderService.agreeAfterSaleApply(apply.getOrderId());
        // 发送消息到消息队列
        OrderReturnApplyAgreeMsgDTO msgDTO = new OrderReturnApplyAgreeMsgDTO(apply.getOrderId(), id);
        orderPublisher.publishReturnApplyAgreeMsg(msgDTO);
    }

    @Override
    public void setCompanyAddress(Long orderReturnApplyId, Long companyAddressId) throws IllegalOrderReturnApplyException {
        OrderReturnApply apply = orderReturnApplyMapper.selectById(orderReturnApplyId);
        if (apply == null) {
            throw new IllegalOrderReturnApplyException("订单退货退款申请不存在");
        }
        CompanyAddress companyAddress = companyAddressService.getById(companyAddressId);
        if (companyAddress == null) {
            throw new IllegalCompanyAddressException("公司地址不存在");
        }
        OrderReturnApply updateApply = new OrderReturnApply();
        updateApply.setId(orderReturnApplyId);
        updateApply.setUpdateTime(new Date());
        updateApply.setCompanyAddressId(companyAddressId);
        updateApply.setCompanyAddressName(companyAddress.getName());
        updateApply.setReceiverName(companyAddress.getReceiverName());
        updateApply.setReceiverPhone(companyAddress.getReceiverPhone());
        updateApply.setProvince(companyAddress.getProvince());
        updateApply.setCity(companyAddress.getCity());
        updateApply.setRegion(companyAddress.getRegion());
        updateApply.setStreet(companyAddress.getStreet());
        updateApply.setDetailAddress(companyAddress.getDetailAddress());
        orderReturnApplyMapper.updateById(updateApply);
    }

    @Override
    public void receive(Long orderReturnApplyId, String receiveNote, Date receiveTime) throws IllegalOrderReturnApplyException {
        OrderReturnApply apply = orderReturnApplyMapper.selectById(orderReturnApplyId);
        if (apply == null) {
            throw new IllegalOrderReturnApplyException("订单退货退款申请不存在");
        }
        OrderReturnApply updateApply = new OrderReturnApply();
        updateApply.setId(orderReturnApplyId);
        updateApply.setUpdateTime(new Date());
        updateApply.setReceiveNote(receiveNote);
        updateApply.setReceiveTime(receiveTime);
        orderReturnApplyMapper.updateById(updateApply);
    }

    @Override
    public void complete(Long id, String handleNote) throws IllegalOrderRefundApplyException {
        OrderReturnApply apply = orderReturnApplyMapper.selectById(id);
        if (apply == null) {
            throw new IllegalOrderReturnApplyException("订单退货退款申请不存在");
        }
        if (!OrderReturnApply.Status.PROCESSING.getValue().equals(apply.getStatus())) {
            throw new IllegalOrderReturnApplyException("当前订单退货退款申请不支持此操作");
        }
        OrderReturnApply updateApply = new OrderReturnApply();
        updateApply.setId(id);
        updateApply.setUpdateTime(new Date());
        updateApply.setHandleTime(new Date());
        updateApply.setHandleNote(handleNote);
        updateApply.setStatus(OrderReturnApply.Status.COMPLETED.getValue());
        orderReturnApplyMapper.updateById(updateApply);
        Long orderId = apply.getOrderId();
        List<OrderReturnApplyProduct> products = orderReturnApplyProductMapper.selectByApplyId(id);
        List<Long> ids = products.stream().map(OrderReturnApplyProduct::getOrderProductId).collect(Collectors.toList());
        orderService.completeAfterSale(orderId, ids);
        // 发送消息到消息队列
        OrderReturnCompleteMsgDTO msgDTO = new OrderReturnCompleteMsgDTO(orderId, id, handleNote);
        orderPublisher.publishReturnCompleteMsg(msgDTO);
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setOrderPublisher(OrderPublisher orderPublisher) {
        this.orderPublisher = orderPublisher;
    }

    @Autowired
    public void setCompanyAddressService(CompanyAddressService companyAddressService) {
        this.companyAddressService = companyAddressService;
    }
}
