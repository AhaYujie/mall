package online.ahayujie.mall.portal.pms.service.impl;

import online.ahayujie.mall.portal.pms.bean.model.Sku;
import online.ahayujie.mall.portal.pms.mapper.SkuMapper;
import online.ahayujie.mall.portal.pms.service.SkuService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author aha
 * @since 2020/10/22
 */
@Service
public class SkuServiceImpl implements SkuService {
    private final SkuMapper skuMapper;

    public SkuServiceImpl(SkuMapper skuMapper) {
        this.skuMapper = skuMapper;
    }

    @Override
    public Map<Long, BigDecimal> getPrice(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }
        List<Sku> skus = skuMapper.selectPrice(ids);
        Map<Long, BigDecimal> map = new HashMap<>();
        for (Sku sku : skus) {
            map.put(sku.getId(), sku.getPrice());
        }
        return map;
    }

    @Override
    public Sku getById(Long id) {
        return skuMapper.selectById(id);
    }
}
