package online.ahayujie.mall.portal.config;

import org.springframework.amqp.core.*;
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

    public static final String ORDER_CANCEL_EXCHANGE = "order.cancel";
    public static final String ORDER_TIMEOUT_CANCEL_QUEUE = "order.timeout.cancel";
    public static final String ORDER_TIMEOUT_CANCEL_ROUTING_KEY = "order.timeout.cancel";

    public static final String ORDER_TTL_EXCHANGE = "order.ttl";
    public static final String ORDER_CANCEL_TTL_QUEUE = "order.cancel.ttl";
    public static final String ORDER_CANCEL_TTL_ROUTING_KEY = "order.cancel.ttl";

    @Bean
    public FanoutExchange productCommentReplyExchange() {
        return new FanoutExchange(PRODUCT_COMMENT_REPLY_EXCHANGE, true, false);
    }

    @Bean
    public DirectExchange orderCancelExchange() {
        return new DirectExchange(ORDER_CANCEL_EXCHANGE, true, false);
    }

    @Bean
    public Queue orderTimeoutCancelQueue() {
        return new Queue(ORDER_TIMEOUT_CANCEL_QUEUE);
    }

    @Bean
    public Binding orderTimeoutCancelBinding() {
        return BindingBuilder
                .bind(orderTimeoutCancelQueue())
                .to(orderCancelExchange())
                .with(ORDER_TIMEOUT_CANCEL_ROUTING_KEY);
    }

    @Bean
    public DirectExchange orderTTLExchange() {
        return new DirectExchange(ORDER_TTL_EXCHANGE, true, false);
    }

    @Bean
    public Queue orderCancelTTLQueue() {
        return QueueBuilder
                .durable(ORDER_CANCEL_TTL_QUEUE)
                .withArgument("x-dead-letter-exchange", ORDER_CANCEL_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", ORDER_TIMEOUT_CANCEL_ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding orderCancelTTLBinding() {
        return BindingBuilder
                .bind(orderCancelTTLQueue())
                .to(orderTTLExchange())
                .with(ORDER_CANCEL_TTL_ROUTING_KEY);
    }
}
