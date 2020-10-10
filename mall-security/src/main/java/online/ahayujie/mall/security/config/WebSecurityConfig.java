package online.ahayujie.mall.security.config;

import online.ahayujie.mall.security.component.DynamicSecurityFilter;
import online.ahayujie.mall.security.component.DynamicSecurityService;
import online.ahayujie.mall.security.component.JwtAccessDeniedHandler;
import online.ahayujie.mall.security.component.JwtAuthenticationEntryPoint;
import online.ahayujie.mall.security.jwt.JwtConfigurer;
import online.ahayujie.mall.security.jwt.JwtUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * spring security配置
 * @author aha
 * @date 2020/3/25
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private JwtConfigurer jwtConfigurer;

    private IgnoreUrlsConfig ignoreUrlsConfig;

    @Autowired
    private JwtUserDetailService jwtUserDetailService;

    private DynamicSecurityFilter dynamicSecurityFilter;

    private DynamicSecurityService dynamicSecurityService;

    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(jwtUserDetailService);
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = httpSecurity
                .authorizeRequests();
        // 不需要保护的资源路径允许访问
        for (String url: ignoreUrlsConfig.getUrls()) {
            registry.antMatchers(url).permitAll();
        }
        //有动态权限配置时添加动态权限校验过滤器
        if (dynamicSecurityService != null) {
            registry.and().addFilterBefore(dynamicSecurityFilter, FilterSecurityInterceptor.class);
        }
        registry
                .and()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .apply(jwtConfigurer);
    }

    @Autowired
    public void setJwtConfigurer(JwtConfigurer jwtConfigurer) {
        this.jwtConfigurer = jwtConfigurer;
    }

    @Autowired
    public void setIgnoreUrlsConfig(IgnoreUrlsConfig ignoreUrlsConfig) {
        this.ignoreUrlsConfig = ignoreUrlsConfig;
    }

    @Autowired(required = false)
    public void setDynamicSecurityFilter(DynamicSecurityFilter dynamicSecurityFilter) {
        this.dynamicSecurityFilter = dynamicSecurityFilter;
    }

    @Autowired(required = false)
    public void setDynamicSecurityService(DynamicSecurityService dynamicSecurityService) {
        this.dynamicSecurityService = dynamicSecurityService;
    }

    @Autowired
    public void setJwtAccessDeniedHandler(JwtAccessDeniedHandler jwtAccessDeniedHandler) {
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Autowired
    public void setJwtAuthenticationEntryPoint(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }
}









