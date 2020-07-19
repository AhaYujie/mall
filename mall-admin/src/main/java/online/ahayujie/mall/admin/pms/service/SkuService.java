package online.ahayujie.mall.admin.pms.service;

import online.ahayujie.mall.admin.pms.bean.dto.ProductSpecificationDTO;
import online.ahayujie.mall.admin.pms.bean.model.Sku;
import online.ahayujie.mall.admin.pms.bean.model.SkuImage;
import online.ahayujie.mall.admin.pms.bean.model.SkuSpecificationRelationship;
import online.ahayujie.mall.admin.pms.exception.IllegalSkuException;

import java.util.List;

/**
 * <p>
 * 商品sku 服务类
 * </p>
 *
 * @author aha
 * @since 2020-07-14
 */
public interface SkuService {
    /**
     * 检查sku合法性
     * @param sku sku信息
     * @throws IllegalSkuException sku信息不合法
     */
    void validate(Sku sku) throws IllegalSkuException;

    /**
     * 生成sku编码。
     * 生成规则：时间(yyyyMMddHHmmssSSS格式) + 2位随机数 + 商品id(最短长度6位，不足6位在前面补0) +
     * {@code index} (取后3位，如果为null，则随机生成3位随机数)。
     * 因为用户可能在一次请求中对一个商品生成多个sku，用 {@code index} 代表用户提交的sku列表索引
     * 可以防止产生相同的sku编码。
     * @param sku sku信息
     * @param index 长度为3为的索引
     * @return sku编码
     * @throws NullPointerException {@code sku} 为null或商品id为null
     */
    String generateSkuCode(Sku sku, Integer index) throws NullPointerException;

    /**
     * 根据商品规格信息生成sku的商品规格json字符串
     * @param specificationDTOS 商品规格信息
     * @return sku的商品规格json字符串
     */
    String generateSpecification(List<ProductSpecificationDTO> specificationDTOS);

    /**
     * 保存sku图片，若 {@code skuImages} 为空则不做操作
     * @param skuImages sku图片
     */
    void saveSkuImages(List<SkuImage> skuImages);

    /**
     * 保存sku和商品规格关系，若 {@code relationships} 为空则不做操作
     * @param relationships sku和商品规格关系
     */
    void saveSkuSpecificationRelationships(List<SkuSpecificationRelationship> relationships);
}
