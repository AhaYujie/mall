package online.ahayujie.mall.portal.oms.service;

import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.portal.oms.bean.dto.AddCartProductParam;
import online.ahayujie.mall.portal.oms.bean.dto.CartProductDTO;

import java.util.List;

/**
 * <p>
 * 购物车商品表 服务类
 * </p>
 *
 * @author aha
 * @since 2020-10-22
 */
public interface CartProductService {
    /**
     * 分页获取会员的购物车列表
     * @param pageNum 页索引
     * @param pageSize 页大小
     * @return 购物车
     */
    CommonPage<CartProductDTO> list(Long pageNum, Long pageSize);

    /**
     * 添加商品到购物车，如果购物车已经存在该商品(sku)，则增加商品购买数量。
     *
     * @param param 商品参数
     * @throws IllegalArgumentException 参数不合法
     */
    void add(AddCartProductParam param) throws IllegalArgumentException;

    /**
     * 更新购物车中商品的数量
     * @param id 购物车商品id
     * @param quantity 数量
     * @throws IllegalArgumentException 参数不合法
     */
    void updateQuantity(Long id, Integer quantity) throws IllegalArgumentException;

    /**
     * 更新购物车中商品的sku
     * @param cartProductId 购物车商品id
     * @param skuId sku的id
     * @throws IllegalArgumentException 参数不合法
     */
    void updateSku(Long cartProductId, Long skuId) throws IllegalArgumentException;

    /**
     * 删除购物车中的商品。
     * 如果某个商品不存在则忽略。
     *
     * @param ids 购物车商品id
     */
    void delete(List<Long> ids);
}
