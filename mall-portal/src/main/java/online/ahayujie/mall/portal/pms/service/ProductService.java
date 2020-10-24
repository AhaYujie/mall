package online.ahayujie.mall.portal.pms.service;

import online.ahayujie.mall.portal.pms.bean.dto.ProductDetailDTO;
import online.ahayujie.mall.portal.pms.bean.dto.SkuDTO;
import online.ahayujie.mall.portal.pms.bean.model.Product;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品信息 服务类
 * </p>
 *
 * @author aha
 * @since 2020-10-17
 */
public interface ProductService {
    /**
     * 获取商品详情。
     * 需要是已上架的商品。
     * 如果商品不存在返回null。
     *
     * @param id 商品id
     * @param isMobile 是否为移动端，0为否，1为是
     * @return 商品详情
     */
    ProductDetailDTO getDetail(Long id, Integer isMobile);

    /**
     * 获取sku的图片。
     * sku对应的商品需要是已上架。
     * 如果不存在则返回null。
     *
     * @param skuId sku主键
     * @return 图片
     */
    List<String> getSkuImages(Long skuId);

    /**
     * 获取商品是否上架。
     * 如果商品不存在，则返回的map不包括这个键值对，即 {@code map.containsKey(id) = false}。
     * 如果ids为空或者null则直接返回null。
     *
     * @param ids 商品id
     * @return 商品id和isPublish的map
     */
    Map<Long, Integer> getIsPublish(List<Long> ids);

    /**
     * 获取已上架的商品信息
     * @param id 商品id
     * @return 商品信息
     */
    Product getById(Long id);

    /**
     * 获取商品的sku。
     * 如果商品不存在或者未上架则返回null。
     *
     * @param id 商品id
     * @return 商品的sku
     */
    SkuDTO getSku(Long id);
}
