package online.ahayujie.mall.admin.pms.service;

import online.ahayujie.mall.admin.oms.bean.dto.CreateOrderParam;
import online.ahayujie.mall.admin.pms.bean.dto.ProductDTO;
import online.ahayujie.mall.admin.pms.bean.dto.ProductSpecificationDTO;
import online.ahayujie.mall.admin.pms.bean.model.Sku;
import online.ahayujie.mall.admin.pms.bean.model.SkuImage;
import online.ahayujie.mall.admin.pms.bean.model.SkuSpecificationRelationship;
import online.ahayujie.mall.admin.pms.exception.IllegalSkuException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 根据商品id获取sku信息，若该商品不存在或该商品无sku信息则返回null。
     * @param productId 商品id
     * @return sku信息
     */
    List<ProductDTO.SkuDTO> getByProductId(Long productId);

    /**
     * 根据sku主键删除sku的图片
     * @param skuId sku主键
     */
    void deleteSkuImage(Long skuId);

    /**
     * 根据id获取sku。
     * 如果sku不存在则返回null。
     * @param id sku的id
     * @return sku
     */
    Sku getById(Long id);

    /**
     * 获取商品全部sku的商品规格关系
     * @param productId 商品id
     * @return 商品全部sku的商品规格关系
     */
    List<List<SkuSpecificationRelationship>> getAllSkuSpecificationRelationships(Long productId);

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
    void updateStock(List<CreateOrderParam.Product> products) throws IllegalArgumentException;
}
