package online.ahayujie.mall.admin.pms.publisher;

import online.ahayujie.mall.admin.config.RabbitmqConfig;
import online.ahayujie.mall.admin.pms.bean.dto.ProductCreateMsgDTO;

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

    /**
     * 发送创建商品消息。
     * exchange为 {@link RabbitmqConfig#PRODUCT_CREATE_EXCHANGE}。
     *
     * @param msgDTO 消息
     */
    void publishCreateMsg(ProductCreateMsgDTO msgDTO);
}
