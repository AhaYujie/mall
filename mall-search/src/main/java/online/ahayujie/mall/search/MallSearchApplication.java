package online.ahayujie.mall.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author aha
 * @since 2020/10/25
 */
@SpringBootApplication
@ComponentScan(basePackages = {"online.ahayujie.mall"})
public class MallSearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallSearchApplication.class, args);
    }
}
