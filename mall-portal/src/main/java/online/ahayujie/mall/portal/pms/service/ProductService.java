package online.ahayujie.mall.portal.pms.service;

import online.ahayujie.mall.portal.pms.bean.dto.ProductDetailDTO;

import java.util.List;

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
     * 需要是已经审核通过的，并且上架的商品。
     * 如果商品不存在返回null。
     *
     * @param id 商品id
     * @param isMobile 是否为移动端，0为否，1为是
     * @return 商品详情
     */
    ProductDetailDTO getDetail(Long id, Integer isMobile);

    /**
     * 获取sku的图片。
     * sku对应的商品需要是已经审核通过的，并且上架。
     * 如果不存在则返回null。
     *
     * @param skuId sku主键
     * @return 图片
     */
    List<String> getSkuImages(Long skuId);
}
