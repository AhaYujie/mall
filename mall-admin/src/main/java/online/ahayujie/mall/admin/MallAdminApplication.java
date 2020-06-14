package online.ahayujie.mall.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 应用启动入口
 * @author aha
 * @date 2020/6/4
 */
@SpringBootApplication
@ComponentScan(basePackages = {"online.ahayujie.mall"})
public class MallAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallAdminApplication.class, args);
    }
}
