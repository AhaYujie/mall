package online.ahayujie.mall.search.service;

import online.ahayujie.mall.search.bean.model.EsProduct;

import java.util.List;

/**
 * @author aha
 * @since 2020/10/26
 */
public interface ProductService {
    /**
     * 保存商品信息到ES
     * @param id 商品id
     */
    void saveEsProduct(Long id);

    /**
     * 保存商品信息到ES
     * @param ids 商品id
     */
    void saveEsProducts(List<Long> ids);

    /**
     * 更新商品信息到ES
     * @param id 商品id
     */
    void updateEsProduct(Long id);

    /**
     * 更新商品信息到ES
     * @param ids 商品id
     */
    void updateEsProducts(List<Long> ids);

    /**
     * 删除ES中的商品
     * @param id 商品id
     */
    void deleteEsProduct(Long id);

    /**
     * 删除ES中的商品
     * @param ids 商品id
     */
    void deleteEsProducts(List<Long> ids);

    EsProduct getById(Long id);
}
