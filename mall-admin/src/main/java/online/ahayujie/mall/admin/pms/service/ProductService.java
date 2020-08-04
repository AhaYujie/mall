package online.ahayujie.mall.admin.pms.service;

import com.rabbitmq.client.Channel;
import online.ahayujie.mall.admin.pms.bean.dto.*;
import online.ahayujie.mall.admin.pms.bean.model.Product;
import online.ahayujie.mall.admin.pms.bean.model.Sku;
import online.ahayujie.mall.admin.pms.exception.*;
import online.ahayujie.mall.common.api.CommonPage;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;

import java.io.IOException;
import java.util.List;

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
     * 若不为空，则用新的商品图片替换旧的商品图片。
     * 更新商品成功后，调用
     * {@link online.ahayujie.mall.admin.pms.publisher.ProductPublisher#publishUpdateMsg(Long)}
     * 发送消息到消息队列
     *
     * @see online.ahayujie.mall.admin.pms.publisher.ProductPublisher#publishUpdateMsg(Long)
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

    /**
     * 分页获取商品列表，根据排序字段和创建时间排序
     * @param pageNum 页索引
     * @param pageSize 页大小
     * @return 商品列表
     */
    CommonPage<Product> list(Integer pageNum, Integer pageSize);

    /**
     * 根据商品id批量更新商品信息。
     * 若某一商品不存在则忽略。
     * 更新商品信息成功后，对每个更新的商品调用
     * {@link online.ahayujie.mall.admin.pms.publisher.ProductPublisher#publishUpdateMsg(Long)}
     * 发送消息到消息队列。
     *
     * @see online.ahayujie.mall.admin.pms.publisher.ProductPublisher#publishUpdateMsg(Long)
     * @param ids 商品id
     * @param param 商品信息
     * @throws IllegalProductException 商品信息不合法
     * @throws IllegalProductCategoryException 商品分类不存在
     * @throws IllegalBrandException 商品品牌不存在
     */
    void updateProductBatch(List<Long> ids, UpdateProductBatchParam param) throws IllegalProductException,
            IllegalProductCategoryException, IllegalBrandException;

    /**
     * 批量更新商品的上下架状态。
     * 如果某个商品不存在则忽略。
     * @param ids 商品id
     * @param publishStatus 商品的上下架状态
     * @throws IllegalProductException 商品的上下架状态不合法
     */
    void updatePublishStatus(List<Long> ids, Integer publishStatus) throws IllegalProductException;

    /**
     * 批量更新商品的推荐状态。
     * 如果某个商品不存在则忽略。
     * @param ids 商品id
     * @param recommendStatus 商品的推荐状态
     * @throws IllegalProductException 商品的推荐状态不合法
     */
    void updateRecommendStatus(List<Long> ids, Integer recommendStatus) throws IllegalProductException;

    /**
     * 批量更新商品的新品状态。
     * 如果某个商品不存在则忽略。
     * @param ids 商品id
     * @param newStatus 商品新品状态
     * @throws IllegalProductException 商品新品状态不合法
     */
    void updateNewStatus(List<Long> ids, Integer newStatus) throws IllegalProductException;

    /**
     * 根据商品id和sku编号模糊查询sku库存信息。
     * 若商品不存在或sku不存在则返回空列表。
     * @param id 商品id
     * @param keyword sku编号关键词，若为null则查询该商品的全部sku。
     * @return sku
     */
    List<Sku> querySku(Long id, String keyword);

    /**
     * 审核商品通过。
     * @param id 商品id
     * @param verifyStatus 审核状态
     * @param note 备注
     * @throws IllegalProductException 商品不存在或审核状态不合法
     */
    void verifyProduct(Long id, Integer verifyStatus, String note) throws IllegalProductException;

    /**
     * 监听商品分类更新的消息。
     * 因为只需要更新商品分类下的商品的商品分类冗余信息，所以不需要保证幂等性。
     * @param channel channel
     * @param message message
     * @throws IOException 确认消息失败
     */
    void listenProductCategoryUpdate(Channel channel, Message message) throws IOException;

    /**
     * 监听商品分类删除消息。
     * 设置该商品分类下的所有商品为无品牌，即修改商品分类id和商品分类名称字段。
     * 不需要保证幂等性。
     * @param channel channel
     * @param message message
     * @throws IOException 确认消息失败
     */
    void listenProductCategoryDelete(Channel channel, Message message) throws IOException;

    /**
     * 监听商品品牌更新消息。
     * 更新该品牌的所有商品的品牌名称冗余字段。
     * 不需要保证幂等性。
     * @param channel channel
     * @param message message
     * @throws IOException 确认消息失败
     */
    void listenBrandUpdate(Channel channel, Message message) throws IOException;

    /**
     * 监听商品品牌删除消息。
     * 更新该品牌的所有商品，设置为无品牌。
     * 不需要保证幂等性。
     * @param channel channel
     * @param message message
     * @throws IOException 确认消息失败
     */
    void listenBrandDelete(Channel channel, Message message) throws IOException;
}
