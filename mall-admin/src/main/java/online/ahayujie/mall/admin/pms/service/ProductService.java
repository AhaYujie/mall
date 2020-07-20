package online.ahayujie.mall.admin.pms.service;

import online.ahayujie.mall.admin.pms.bean.dto.CreateProductParam;
import online.ahayujie.mall.admin.pms.bean.dto.ProductDTO;
import online.ahayujie.mall.admin.pms.bean.model.Product;
import online.ahayujie.mall.admin.pms.exception.*;

/**
 * <p>
 * 商品信息 服务类
 * </p>
 *
 * @author aha
 * @since 2020-07-14
 */
public interface ProductService {
    /**
     * 创建商品
     * @param param 商品
     * @throws IllegalProductException 商品信息不合法
     * @throws IllegalProductSpecificationException 商品规格不合法
     * @throws IllegalProductParamException 商品参数不合法
     * @throws IllegalSkuException sku不合法
     * @throws IllegalProductCategoryException 商品分类不存在
     * @throws IllegalBrandException 商品品牌不存在
     */
    void create(CreateProductParam param) throws IllegalProductException, IllegalProductSpecificationException,
            IllegalProductParamException, IllegalSkuException, IllegalProductCategoryException, IllegalBrandException;

    /**
     * 检查商品信息合法性。
     * 如果某一个字段为null，则忽略该字段不检查。
     * @param product 商品信息
     * @throws IllegalProductException 商品信息不合法
     * @throws IllegalProductCategoryException 商品分类不存在
     * @throws IllegalBrandException 商品品牌不存在
     */
    void validateProduct(Product product) throws IllegalProductException, IllegalProductCategoryException,
            IllegalBrandException;

    /**
     * 根据商品id获取商品信息。
     * 若商品不存在则返回null。
     * 若无商品参数信息或商品规格信息或商品sku信息则均设置字段为null。
     * @param id 商品id
     * @return 商品信息
     */
    ProductDTO getProductById(Long id);
}
