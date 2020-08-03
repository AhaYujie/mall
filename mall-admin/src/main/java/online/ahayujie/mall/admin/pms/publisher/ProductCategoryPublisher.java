package online.ahayujie.mall.admin.pms.publisher;

import online.ahayujie.mall.admin.pms.bean.dto.DeleteProductCategoryMessageDTO;

/**
 * 商品分类消息发送者
 * @author aha
 * @since 2020/8/3
 */
public interface ProductCategoryPublisher {
    /**
     * 发送更新商品分类消息到消息队列。
     * 消息格式：{@link online.ahayujie.mall.admin.pms.bean.dto.UpdateProductCategoryMessageDTO} 转为json
     * @param id 商品分类id
     */
    void publishUpdateMsg(Long id);

    /**
     * 发送删除商品分类消息到消息队列。
     * 消息格式：{@link DeleteProductCategoryMessageDTO} 转为json
     * @param messageDTO 删除的商品分类信息
     */
    void publishDeleteMsg(DeleteProductCategoryMessageDTO messageDTO);
}
