package online.ahayujie.mall.admin.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author aha
 * @date 2020/3/26
 */
@Configuration
public class OssConfig {

    @Value("${oss.endpoint}")
    private String ossEndpoint;

    @Value("${oss.access-key-id}")
    private String accessKeyId;

    @Value("${oss.access-key-secret}")
    private String accessKeySecret;

    @Bean
    public OSS oss() {
        return new OSSClientBuilder().build(ossEndpoint, accessKeyId, accessKeySecret);
    }

}
