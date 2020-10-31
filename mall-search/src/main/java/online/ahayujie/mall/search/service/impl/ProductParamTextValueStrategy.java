package online.ahayujie.mall.search.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import online.ahayujie.mall.search.bean.model.ProductParam;
import online.ahayujie.mall.search.service.ProductParamValueStrategy;
import org.springframework.stereotype.Service;

/**
 * @author aha
 * @since 2020/10/27
 */
@Service(value = ProductParam.TEXT_TYPE)
public class ProductParamTextValueStrategy implements ProductParamValueStrategy {
    private final ObjectMapper objectMapper;

    public ProductParamTextValueStrategy(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String getTextValue(String value) {
        try {
            ProductParamTextValue textValue = objectMapper.readValue(value, ProductParamTextValue.class);
            return textValue.getName();
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @Data
    private static class ProductParamTextValue {
        private String name;
    }
}
