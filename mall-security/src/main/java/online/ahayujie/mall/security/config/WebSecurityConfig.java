package online.ahayujie.mall.security.config;

import online.ahayujie.mall.security.component.JwtAccessDeniedHandler;
import online.ahayujie.mall.security.component.JwtAuthenticationEntryPoint;
import online.ahayujie.mall.security.jwt.JwtConfigurer;
import online.ahayujie.mall.security.jwt.JwtUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * spring security配置
 * @author aha
 * @date 2020/3/25
 */
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtConfigurer jwtConfigurer;

    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;

    @Autowired
    private JwtUserDetailService jwtUserDetailService;

    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(jwtUserDetailService);
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        // 不需要保护的资源路径允许访问
        for (String url: ignoreUrlsConfig.getUrls()) {
            httpSecurity.authorizeRequests().antMatchers(url).permitAll();
        }
        httpSecurity
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

}









