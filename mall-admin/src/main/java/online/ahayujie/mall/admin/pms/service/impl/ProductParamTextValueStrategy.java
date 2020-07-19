package online.ahayujie.mall.admin.pms.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import online.ahayujie.mall.admin.pms.bean.model.ProductParam;
import online.ahayujie.mall.admin.pms.exception.IllegalProductParamException;
import online.ahayujie.mall.admin.pms.service.ProductParamValueStrategy;
import org.springframework.stereotype.Service;

/**
 * 纯文本类型的商品参数策略实现类。
 * json例子：{"name" : "test"}
 * @author aha
 * @since 2020/7/15
 */
@Service(value = ProductParam.TEXT_TYPE)
public class ProductParamTextValueStrategy implements ProductParamValueStrategy {
    private final ObjectMapper objectMapper;

    public ProductParamTextValueStrategy(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void validate(String value) throws IllegalProductParamException {
        try {
            ProductParamTextValue textValue = objectMapper.readValue(value, ProductParamTextValue.class);
            if (textValue == null || textValue.getName() == null) {
                throw new IllegalProductParamException("纯文本类型的商品参数值不合法: " + value);
            }
        } catch (JsonProcessingException e) {
            throw new IllegalProductParamException("纯文本类型的商品参数值不合法: " + value);
        }
    }

    @Data
    private static class ProductParamTextValue {
        private String name;
    }
}
