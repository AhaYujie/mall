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

    public static final String PRODUCT_CATEGORY_DELETE_EXCHANGE = "product.category.delete";

    public static final String BRAND_UPDATE_EXCHANGE = "brand.update";

    public static final String BRAND_DELETE_EXCHANGE = "brand.delete";

    @Bean
    public FanoutExchange productCategoryUpdateFanoutExchange() {
        return new FanoutExchange(PRODUCT_CATEGORY_UPDATE_EXCHANGE, true, false);
    }

    @Bean
    public FanoutExchange productCategoryDeleteFanoutExchange() {
        return new FanoutExchange(PRODUCT_CATEGORY_DELETE_EXCHANGE, true, false);
    }

    @Bean
    public FanoutExchange brandUpdateExchange() {
        return new FanoutExchange(BRAND_UPDATE_EXCHANGE, true, false);
    }

    @Bean
    public FanoutExchange brandDeleteExchange() {
        return new FanoutExchange(BRAND_DELETE_EXCHANGE, true, false);
    }
}
