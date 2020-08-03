package online.ahayujie.mall.admin.pms.publisher;

import online.ahayujie.mall.admin.pms.bean.dto.DeleteBrandMessageDTO;
import online.ahayujie.mall.admin.pms.bean.dto.UpdateBrandMessageDTO;

/**
 * 商品品牌消息发送者
 * @author aha
 * @since 2020/8/3
 */
public interface BrandPublisher {
    /**
     * 发送更新商品品牌消息到消息队列。
     * 消息格式：{@link UpdateBrandMessageDTO} 转为json
     * @param id 商品品牌id
     */
    void publishUpdateMsg(Long id);

    /**
     * 发送删除商品品牌消息到消息队列。
     * 消息格式：{@link DeleteBrandMessageDTO} 转为json
     * @param messageDTO 删除的商品品牌信息
     */
    void publishDeleteMsg(DeleteBrandMessageDTO messageDTO);
}
