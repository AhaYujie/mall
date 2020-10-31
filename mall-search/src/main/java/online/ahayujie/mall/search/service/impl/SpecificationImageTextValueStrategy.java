package online.ahayujie.mall.search.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import online.ahayujie.mall.search.bean.model.ProductSpecificationValue;
import online.ahayujie.mall.search.service.SpecificationValueStrategy;
import org.springframework.stereotype.Service;

/**
 * 图文类型的商品规格选项值策略实现类。
 * json例子：{"name" : "test", "image" : "http://test.jpg"}
 *
 * @author aha
 * @since 2020/10/28
 */
@Service(value = ProductSpecificationValue.IMAGE_TEXT_TYPE_NAME)
public class SpecificationImageTextValueStrategy implements SpecificationValueStrategy {
    private final ObjectMapper objectMapper;

    public SpecificationImageTextValueStrategy(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String getTextValue(String value) {
        try {
            SpecificationImageTextValue imageTextValue = objectMapper.readValue(value, SpecificationImageTextValue.class);
            return imageTextValue.getName();
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @Data
    private static class SpecificationImageTextValue {
        private String name;
        private String image;
    }
}
