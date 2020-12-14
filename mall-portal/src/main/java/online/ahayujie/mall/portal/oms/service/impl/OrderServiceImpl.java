package online.ahayujie.mall.portal.oms.service.impl;

import online.ahayujie.mall.portal.mapper.DictMapper;
import online.ahayujie.mall.portal.mms.bean.model.Member;
import online.ahayujie.mall.portal.mms.bean.model.ReceiveAddress;
import online.ahayujie.mall.portal.mms.service.MemberService;
import online.ahayujie.mall.portal.mms.service.ReceiveAddressService;
import online.ahayujie.mall.portal.oms.bean.dto.ConfirmOrderDTO;
import online.ahayujie.mall.portal.oms.bean.dto.GenerateConfirmOrderParam;
import online.ahayujie.mall.portal.oms.bean.dto.IntegrationRule;
import online.ahayujie.mall.portal.oms.service.OrderService;
import online.ahayujie.mall.portal.pms.bean.model.Sku;
import online.ahayujie.mall.portal.pms.mapper.SkuMapper;
import online.ahayujie.mall.portal.pms.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author aha
 * @since 2020-11-07
 */
@Service
public class OrderServiceImpl implements OrderService {
    private static final String INTEGRATION_DICT_CODE = "integration-setting";
    private static final String INTEGRATION_RATIO_DICT_KEY = "1";
    private static final String INTEGRATION_UNIT_DICT_KEY = "2";

    private MemberService memberService;
    private ProductService productService;
    private ReceiveAddressService receiveAddressService;

    private final SkuMapper skuMapper;
    private final DictMapper dictMapper;

    public OrderServiceImpl(SkuMapper skuMapper, DictMapper dictMapper) {
        this.skuMapper = skuMapper;
        this.dictMapper = dictMapper;
    }

    @Override
    public ConfirmOrderDTO generateConfirmOrder(GenerateConfirmOrderParam param) throws IllegalArgumentException {
        // 商品信息检验和获取
        if (CollectionUtils.isEmpty(param.getProducts())) {
            throw new IllegalArgumentException("购买的商品不能为空");
        }
        List<Long> skuIds = param.getProducts().stream().map(GenerateConfirmOrderParam.Product::getSkuId).collect(Collectors.toList());
        for (GenerateConfirmOrderParam.Product product : param.getProducts()) {
            if (product.getQuantity() <= 0) {
                throw new IllegalArgumentException("购买商品的数量小于等于0");
            }
        }
        List<Sku> skus = skuMapper.selectBatchIds(skuIds);
        if (skuIds.size() != skus.size()) {
            List<Long> exists = skus.stream().map(Sku::getId).collect(Collectors.toList());
            for (Long skuId : skuIds) {
                if (!exists.contains(skuId)) {
                    throw new IllegalArgumentException("商品不存在: " + skuId);
                }
            }
        }
        List<Long> productIds = skus.stream().map(Sku::getProductId).collect(Collectors.toList());
        List<ConfirmOrderDTO.Product> products = productService.getConfirmOrderProductBatch(productIds);
        if (skus.size() != products.size()) {
            List<Long> existProductIds = products.stream().map(ConfirmOrderDTO.Product::getId).collect(Collectors.toList());
            for (Sku sku : skus) {
                if (!existProductIds.contains(sku.getProductId())) {
                    throw new IllegalArgumentException("商品未上架: " + sku.getId());
                }
            }
        }
        for (ConfirmOrderDTO.Product product : products) {
            for (Sku sku : skus) {
                if (sku.getProductId().equals(product.getId())) {
                    product.setSkuId(sku.getId());
                    product.setPic(sku.getPic());
                    product.setSpecification(sku.getSpecification());
                    product.setPrice(sku.getPrice());
                    break;
                }
            }
            for (GenerateConfirmOrderParam.Product productParam : param.getProducts()) {
                if (productParam.getSkuId().equals(product.getSkuId())) {
                    product.setQuantity(productParam.getQuantity());
                    break;
                }
            }
        }

        // 会员收货地址检验和获取
        Member member = memberService.getMemberFromToken();
        ReceiveAddress receiveAddress = receiveAddressService.getById(param.getReceiveAddressId(), member.getId());
        if (receiveAddress == null) {
            throw new IllegalArgumentException("收货地址不存在");
        }
        ConfirmOrderDTO.ReceiveAddress receiveAddressDTO = new ConfirmOrderDTO.ReceiveAddress();
        BeanUtils.copyProperties(receiveAddress, receiveAddressDTO);

        // 积分信息检验和获取
        ConfirmOrderDTO.IntegrationInfo integrationInfo = new ConfirmOrderDTO.IntegrationInfo();
        integrationInfo.setIntegration(memberService.getInfo().getIntegration());
        Integer usedIntegration = param.getUsedIntegration();
        integrationInfo.setUsedIntegration(usedIntegration);
        if (usedIntegration < 0 || usedIntegration > integrationInfo.getIntegration()) {
            throw new IllegalArgumentException("使用的积分数量不合法");
        }
        Integer maxUseIntegration = 0;
        for (ConfirmOrderDTO.Product product : products) {
            maxUseIntegration += product.getUsePointLimit();
        }
        if (usedIntegration > maxUseIntegration) {
            throw new IllegalArgumentException("使用超过可以抵扣的积分数量");
        }
        integrationInfo.setMaxUseIntegration(maxUseIntegration);
        IntegrationRule integrationRule = getIntegrationRule();
        integrationInfo.setIntegrationRatio(integrationRule.getIntegrationRatio());
        integrationInfo.setIntegrationUseUnit(integrationRule.getIntegrationUseUnit());
        if (usedIntegration % integrationInfo.getIntegrationUseUnit() != 0) {
            throw new IllegalArgumentException("使用的积分数量不合法");
        }

        // 计算金额
        BigDecimal productAmount = new BigDecimal("0");
        for (ConfirmOrderDTO.Product product : products) {
            productAmount = productAmount.add(product.getPrice().multiply(new BigDecimal(product.getQuantity())));
        }
        BigDecimal integrationAmount = new BigDecimal(integrationInfo.getUsedIntegration())
                .divide(integrationInfo.getIntegrationRatio(), 2, BigDecimal.ROUND_HALF_UP);
        // TODO:计算订单运费
        BigDecimal freightAmount = new BigDecimal("0");
        BigDecimal payAmount = productAmount.add(freightAmount).subtract(integrationAmount);
        ConfirmOrderDTO.Amount amount = new ConfirmOrderDTO.Amount();
        amount.setIntegrationAmount(integrationAmount);
        amount.setProductAmount(productAmount);
        amount.setFreightAmount(freightAmount);
        amount.setPayAmount(payAmount);

        ConfirmOrderDTO confirmOrderDTO = new ConfirmOrderDTO();
        confirmOrderDTO.setProducts(products);
        confirmOrderDTO.setReceiveAddress(receiveAddressDTO);
        confirmOrderDTO.setIntegrationInfo(integrationInfo);
        confirmOrderDTO.setAmount(amount);
        return confirmOrderDTO;
    }

    @Override
    public IntegrationRule getIntegrationRule() {
        IntegrationRule integrationRule = new IntegrationRule();
        integrationRule.setIntegrationRatio(new BigDecimal(dictMapper.selectByCodeAndDictKey(INTEGRATION_DICT_CODE,
                INTEGRATION_RATIO_DICT_KEY).getDictValue()));
        integrationRule.setIntegrationUseUnit(Integer.valueOf(dictMapper.selectByCodeAndDictKey(INTEGRATION_DICT_CODE,
                INTEGRATION_UNIT_DICT_KEY).getDictValue()));
        return integrationRule;
    }

    @Autowired
    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setReceiveAddressService(ReceiveAddressService receiveAddressService) {
        this.receiveAddressService = receiveAddressService;
    }
}
