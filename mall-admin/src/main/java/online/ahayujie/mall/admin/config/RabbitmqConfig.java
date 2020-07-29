package online.ahayujie.mall.admin.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
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

    @Bean
    public FanoutExchange productCategoryFanoutExchange() {
        return new FanoutExchange(PRODUCT_CATEGORY_UPDATE_EXCHANGE, true, false);
    }
}
