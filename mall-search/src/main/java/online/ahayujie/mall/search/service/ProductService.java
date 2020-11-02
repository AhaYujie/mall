package online.ahayujie.mall.search.service;

import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.search.bean.model.EsProduct;

import java.math.BigDecimal;
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

    /**
     * 简单搜索。
     *
     * @param pageNum 页索引
     * @param pageSize 页大小
     * @param keyword 关键词
     * @param sort 排序：0->按相关度排序；1->按销量从高到低排序；2->按价格从高到低排序；3->按价格从低到高排序
     * @return 商品
     */
    CommonPage<EsProduct> search(Integer pageNum, Integer pageSize, String keyword, Integer sort);

    /**
     * 搜索商品
     *
     * @param pageNum 页索引
     * @param pageSize 页大小
     * @param keyword 关键词
     * @param brandId 品牌id，为null时不作为搜索条件
     * @param productCategoryId 商品分类id，为null时不作为搜索条件
     * @param productSn 商品货号，为null时不作为搜索条件
     * @param isPublish 是否上架，为null时不作为搜索条件
     * @param isNew 是否新品，为null时不作为搜索条件
     * @param isRecommend 是否推荐，为null时不作为搜索条件
     * @param isVerify 是否审核，为null时不作为搜索条件
     * @param isPreview 是否预告商品，为null时不作为搜索条件
     * @param minPrice 最低价格，为null时不作为搜索条件
     * @param maxPrice 最高价格，为null时不作为搜索条件
     * @param sort sort：0->按相关度排序；1->按销量从高到低排序；2->按价格从高到低排序；3->按价格从低到高排序
     * @return 商品
     */
    CommonPage<EsProduct> search(Integer pageNum, Integer pageSize, String keyword, Long brandId, Long productCategoryId,
                                 String productSn, Integer isPublish, Integer isNew, Integer isRecommend, Integer isVerify,
                                 Integer isPreview, BigDecimal minPrice, BigDecimal maxPrice, Integer sort);

    /**
     * 根据商品id获取推荐商品。
     * 如果商品不存在则返回null。
     *
     * @param pageNum 页索引
     * @param pageSize 页大小
     * @param id 商品id
     * @return 推荐的商品
     */
    CommonPage<EsProduct> recommend(Integer pageNum, Integer pageSize, Long id);
}
