package online.ahayujie.mall.search.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 消息队列配置
 * @author aha
 * @since 2020/10/31
 */
@Configuration
public class RabbitmqConfig {
    public static final String PRODUCT_CREATE_EXCHANGE = "product.create";
    public static final String PRODUCT_CREATE_QUEUE_ES_SAVE = "product.create.es.save";

    public static final String PRODUCT_UPDATE_EXCHANGE = "product.update";
    public static final String PRODUCT_UPDATE_QUEUE_ES_UPDATE = "product.update.es.update";

    @Bean
    public FanoutExchange productCreateExchange() {
        return new FanoutExchange(PRODUCT_CREATE_EXCHANGE, true, false);
    }

    @Bean
    public Queue productCreateQueueEsSave() {
        return new Queue(PRODUCT_CREATE_QUEUE_ES_SAVE);
    }

    @Bean
    public Binding productCreateQueueEsSaveBinding() {
        return BindingBuilder
                .bind(productCreateQueueEsSave())
                .to(productCreateExchange());
    }

    @Bean
    public FanoutExchange productUpdateExchange() {
        return new FanoutExchange(PRODUCT_UPDATE_EXCHANGE, true, false);
    }

    @Bean
    public Queue productUpdateQueueEsUpdate() {
        return new Queue(PRODUCT_UPDATE_QUEUE_ES_UPDATE);
    }

    @Bean
    public Binding productUpdateQueueEsUpdateBinding() {
        return BindingBuilder
                .bind(productUpdateQueueEsUpdate())
                .to(productUpdateExchange());
    }
}
