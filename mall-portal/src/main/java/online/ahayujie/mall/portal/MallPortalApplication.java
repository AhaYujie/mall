package online.ahayujie.mall.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author aha
 * @since 2020/10/8
 */
@EnableAsync
@SpringBootApplication
@ComponentScan(basePackages = {"online.ahayujie.mall"})
public class MallPortalApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallPortalApplication.class, args);
    }
}
