package online.ahayujie.mall.admin.config;

import online.ahayujie.mall.security.component.DynamicAccessDecisionManager;
import online.ahayujie.mall.security.component.DynamicSecurityFilter;
import online.ahayujie.mall.security.component.DynamicSecurityMetadataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author aha
 * @date 2020/6/4
 */
@Configuration
public class AdminSecurityConfig {
    @Bean
    public DynamicSecurityFilter dynamicSecurityFilter() {
        return new DynamicSecurityFilter();
    }

    @Bean
    public DynamicSecurityMetadataSource dynamicSecurityMetadataSource() {
        return new DynamicSecurityMetadataSource();
    }

    @Bean
    public DynamicAccessDecisionManager dynamicAccessDecisionManager() {
        return new DynamicAccessDecisionManager();
    }
}
