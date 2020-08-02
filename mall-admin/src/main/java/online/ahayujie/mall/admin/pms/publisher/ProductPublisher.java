package online.ahayujie.mall.admin.pms.publisher;

/**
 * 商品消息发送者
 * @author aha
 * @since 2020/8/2
 */
public interface ProductPublisher {
    /**
     * 发送更新商品信息消息。
     * 消息格式：{@link online.ahayujie.mall.admin.pms.bean.dto.UpdateProductMessageDTO} 转为json
     * @param id 更新的商品id
     */
    void publishUpdateMsg(Long id);
}
