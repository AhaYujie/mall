package online.ahayujie.mall.search.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import online.ahayujie.mall.search.bean.model.ProductSpecificationValue;
import online.ahayujie.mall.search.service.SpecificationValueStrategy;
import org.springframework.stereotype.Service;

/**
 * 纯文本类型的商品规格选项值策略实现类。
 * json例子：{"name" : "test"}
 *
 * @author aha
 * @since 2020/10/28
 */
@Service(value = ProductSpecificationValue.TEXT_TYPE_NAME)
public class SpecificationTextValueStrategy implements SpecificationValueStrategy {
    private final ObjectMapper objectMapper;

    public SpecificationTextValueStrategy(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String getTextValue(String value) {
        try {
            SpecificationTextValue textValue = objectMapper.readValue(value, SpecificationTextValue.class);
            return textValue.getName();
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @Data
    private static class SpecificationTextValue {
        private String name;
    }
}
