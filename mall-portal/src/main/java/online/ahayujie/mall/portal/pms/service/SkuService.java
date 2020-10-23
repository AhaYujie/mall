package online.ahayujie.mall.portal.pms.service;

import online.ahayujie.mall.portal.pms.bean.model.Sku;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * sku service
 * @author aha
 * @since 2020/10/22
 */
public interface SkuService {
    /**
     * 获取sku的价格。
     * 如果sku不存在则返回的map不包含此键值对，即 {@code map.containsKey(id) = false}。
     * 如果ids为null或空，则直接返回null。
     *
     * @param ids sku主键
     * @return sku的id和价格map
     */
    Map<Long, BigDecimal> getPrice(List<Long> ids);

    /**
     * 根据id获取sku
     * @param id sku的id
     * @return sku
     */
    Sku getById(Long id);
}
