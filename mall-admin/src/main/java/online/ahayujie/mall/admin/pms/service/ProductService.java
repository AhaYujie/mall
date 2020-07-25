package online.ahayujie.mall.admin.pms.service;

import online.ahayujie.mall.admin.pms.bean.dto.*;
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
     * 检查商品是否存在
     * @param id 商品id
     * @throws IllegalProductException 商品不存在
     */
    void validateProduct(Long id) throws IllegalProductException;

    /**
     * 根据商品id获取商品信息。
     * 若商品不存在则返回null。
     * 若无商品参数信息或商品规格信息或商品sku信息则均设置字段为null。
     * @param id 商品id
     * @return 商品信息
     */
    ProductDTO getProductById(Long id);

    /**
     * 根据商品id更新商品信息。
     * 若 {@code param} 中的商品图片为空，则删除全部商品图片，
     * 若不为空，则用新的商品图片替换旧的商品图片
     * @param id 商品id
     * @param param 商品信息
     * @throws IllegalProductException 商品不存在或商品信息不合法
     * @throws IllegalProductCategoryException 商品分类不存在
     * @throws IllegalBrandException 商品品牌不存在
     */
    void updateProduct(Long id, UpdateProductParam param) throws IllegalProductException, IllegalProductCategoryException,
            IllegalBrandException;

    /**
     * 根据商品id更新商品参数信息。
     * 商品参数列表中：1. 参数id不为null的为要更新的商品参数，若某商品参数不存在则忽略，
     *                  若某商品参数的商品id和 {@code id} 不相等则忽略。
     *               2. 参数id为null的为要新增的商品参数。
     *               3. 剩下的商品参数为要删除的。
     * @param id 商品id
     * @param param 商品参数信息
     * @throws IllegalProductException 商品不存在
     * @throws IllegalProductParamException 商品参数信息不合法
     */
    void updateParam(Long id, UpdateProductParamParam param) throws IllegalProductException, IllegalProductParamException;

    /**
     * 根据商品id更新商品规格信息。
     * 只能对已有的商品规格新增选项，若某一商品规格的新增选项列表为空则忽略。
     * @param id 商品id
     * @param param 商品规格信息
     * @throws IllegalProductException 商品不存在
     * @throws IllegalProductSpecificationException 商品规格信息不合法或商品规格不存在
     */
    void updateSpecification(Long id, UpdateProductSpecificationParam param) throws IllegalProductException,
            IllegalProductSpecificationException;

    /**
     * 根据商品id更新商品sku信息。
     * 传id的为更新sku信息，不传id的为新增sku。
     * @param id 商品id
     * @param param 商品sku信息
     * @throws IllegalProductException 商品不存在
     * @throws IllegalSkuException 商品sku信息不合法
     * @throws IllegalProductSpecificationException 新增的sku的商品规格不合法
     */
    void updateSku(Long id, UpdateSkuParam param) throws IllegalProductException, IllegalSkuException,
            IllegalProductSpecificationException;
}
