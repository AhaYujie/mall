package online.ahayujie.mall.portal.pms.service;

import online.ahayujie.mall.portal.oms.bean.dto.SubmitOrderParam;
import online.ahayujie.mall.portal.pms.bean.model.Sku;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 扣减库存。
     * 如果某一个商品扣减库存失败，则回滚已经进行的扣减库存操作，
     * 并抛出 {@link IllegalArgumentException} 异常。
     * 这是一个事务接口，如果调用此接口的上层方法也是事务方法，
     * 且抛出 {@link Exception} 异常，则此接口已经进行的扣减库存操作也会回滚。
     *
     * @param products 需要扣减库存的商品
     * @throws IllegalArgumentException 扣减库存失败
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    void updateStock(List<SubmitOrderParam.Product> products) throws IllegalArgumentException;
}
