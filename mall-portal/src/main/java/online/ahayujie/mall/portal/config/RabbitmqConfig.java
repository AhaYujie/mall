package online.ahayujie.mall.portal.config;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 消息队列配置
 * @author aha
 * @since 2020/10/21
 */
@Configuration
public class RabbitmqConfig {
    public static final String PRODUCT_COMMENT_REPLY_EXCHANGE = "product.comment.reply";

    @Bean
    public FanoutExchange productCommentReplyExchange() {
        return new FanoutExchange(PRODUCT_COMMENT_REPLY_EXCHANGE, true, false);
    }
}
