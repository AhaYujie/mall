package online.ahayujie.mall.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 应用启动入口
 * @author aha
 * @date 2020/6/4
 */
@EnableAsync
@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = {"online.ahayujie.mall"})
public class MallAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallAdminApplication.class, args);
    }
}
