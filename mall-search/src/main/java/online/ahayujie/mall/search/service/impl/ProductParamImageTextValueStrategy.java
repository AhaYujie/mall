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
@Service(value = ProductParam.IMAGE_TEXT_TYPE)
public class ProductParamImageTextValueStrategy implements ProductParamValueStrategy {
    private final ObjectMapper objectMapper;

    public ProductParamImageTextValueStrategy(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String getTextValue(String value) {
        try {
            ProductParamImageTextValue imageTextValue = objectMapper.readValue(value, ProductParamImageTextValue.class);
            return imageTextValue.getName();
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @Data
    private static class ProductParamImageTextValue {
        private String name;
        private String image;
    }
}
