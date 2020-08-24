package online.ahayujie.mall.admin.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 消息队列配置
 * @author aha
 * @since 2020/7/27
 */
@Configuration
public class RabbitmqConfig {
    public static final String PRODUCT_CATEGORY_UPDATE_EXCHANGE = "product.category.update";
    public static final String PRODUCT_CATEGORY_UPDATE_QUEUE_PRODUCT = "product.category.update.product";

    public static final String PRODUCT_CATEGORY_DELETE_EXCHANGE = "product.category.delete";
    public static final String PRODUCT_CATEGORY_DELETE_QUEUE_PRODUCT = "product.category.delete.product";

    public static final String BRAND_UPDATE_EXCHANGE = "brand.update";
    public static final String BRAND_UPDATE_QUEUE_PRODUCT = "brand.update.product";

    public static final String BRAND_DELETE_EXCHANGE = "brand.delete";
    public static final String BRAND_DELETE_QUEUE_PRODUCT = "brand.delete.product";

    public static final String PRODUCT_UPDATE_EXCHANGE = "product.update";

    public static final String ORDER_CANCEL_EXCHANGE = "order.cancel";
    public static final String ORDER_TIMEOUT_CANCEL_QUEUE = "order.timeout.cancel";
    public static final String ORDER_TIMEOUT_CANCEL_ROUTING_KEY = "order.timeout.cancel";
    public static final String ORDER_MEMBER_CANCEL_QUEUE = "order.member.cancel";
    public static final String ORDER_MEMBER_CANCEL_ROUTING_KEY = "order.member.cancel";

    public static final String ORDER_TTL_EXCHANGE = "order.ttl";
    public static final String ORDER_CANCEL_TTL_QUEUE = "order.cancel.ttl";
    public static final String ORDER_CANCEL_TTL_ROUTING_KEY = "order.cancel.ttl";

    public static final String ORDER_CANCELLED_EXCHANGE = "order.cancelled";

    @Bean
    public FanoutExchange productCategoryUpdateFanoutExchange() {
        return new FanoutExchange(PRODUCT_CATEGORY_UPDATE_EXCHANGE, true, false);
    }

    @Bean
    public Queue productCategoryUpdateProductQueue() {
        return new Queue(PRODUCT_CATEGORY_UPDATE_QUEUE_PRODUCT);
    }

    @Bean
    public Binding productCategoryUpdateProductBinding() {
        return BindingBuilder
                .bind(productCategoryUpdateProductQueue())
                .to(productCategoryUpdateFanoutExchange());
    }

    @Bean
    public FanoutExchange productCategoryDeleteFanoutExchange() {
        return new FanoutExchange(PRODUCT_CATEGORY_DELETE_EXCHANGE, true, false);
    }

    @Bean
    public Queue productCategoryDeleteProductQueue() {
        return new Queue(PRODUCT_CATEGORY_DELETE_QUEUE_PRODUCT);
    }

    @Bean
    public Binding productCategoryDeleteProductBinding() {
        return BindingBuilder
                .bind(productCategoryDeleteProductQueue())
                .to(productCategoryDeleteFanoutExchange());
    }

    @Bean
    public FanoutExchange brandUpdateExchange() {
        return new FanoutExchange(BRAND_UPDATE_EXCHANGE, true, false);
    }

    @Bean
    public Queue brandUpdateProductQueue() {
        return new Queue(BRAND_UPDATE_QUEUE_PRODUCT);
    }

    @Bean
    public Binding brandUpdateProductBinding() {
        return BindingBuilder
                .bind(brandUpdateProductQueue())
                .to(brandUpdateExchange());
    }

    @Bean
    public FanoutExchange brandDeleteExchange() {
        return new FanoutExchange(BRAND_DELETE_EXCHANGE, true, false);
    }

    @Bean
    public Queue brandDeleteProductQueue() {
        return new Queue(BRAND_DELETE_QUEUE_PRODUCT);
    }

    @Bean
    public Binding brandDeleteProductBinding() {
        return BindingBuilder
                .bind(brandDeleteProductQueue())
                .to(brandDeleteExchange());
    }

    @Bean
    public FanoutExchange productUpdateExchange() {
        return new FanoutExchange(PRODUCT_UPDATE_EXCHANGE, true, false);
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
    public Queue orderMemberCancelQueue() {
        return new Queue(ORDER_MEMBER_CANCEL_QUEUE);
    }

    @Bean
    public Binding orderMemberCancelBinding() {
        return BindingBuilder
                .bind(orderMemberCancelQueue())
                .to(orderCancelExchange())
                .with(ORDER_MEMBER_CANCEL_ROUTING_KEY);
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

    @Bean
    public FanoutExchange orderCancelledExchange() {
        return new FanoutExchange(ORDER_CANCELLED_EXCHANGE, true, false);
    }
}
