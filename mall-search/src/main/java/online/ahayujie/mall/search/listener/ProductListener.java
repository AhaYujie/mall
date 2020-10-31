package online.ahayujie.mall.search.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

import java.io.IOException;

/**
 * 商品消息监听者
 * @author aha
 * @since 2020/10/31
 */
public interface ProductListener {
    /**
     * 监听商品创建消息
     * @param channel channel
     * @param message message
     * @throws IOException 确认消息失败
     */
    void listenProductCreate(Channel channel, Message message) throws IOException;

    /**
     * 监听商品更新消息
     * @param channel channel
     * @param message message
     * @throws IOException 确认消息失败
     */
    void listenProductUpdate(Channel channel, Message message) throws IOException;
}
