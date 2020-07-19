package online.ahayujie.mall.admin.pms.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.pms.bean.model.ProductSpecificationValue;
import online.ahayujie.mall.admin.pms.exception.IllegalProductSpecificationException;
import online.ahayujie.mall.admin.pms.service.SpecificationValueStrategy;
import org.springframework.stereotype.Service;

/**
 * 纯文本类型的商品规格选项值策略实现类。
 * json例子：{"name" : "test"}
 * @author aha
 * @since 2020/7/15
 */
@Slf4j
@Service(value = ProductSpecificationValue.TEXT_TYPE_NAME)
public class SpecificationTextValueStrategy implements SpecificationValueStrategy {
    private final ObjectMapper objectMapper;

    public SpecificationTextValueStrategy(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void validate(String value) throws IllegalProductSpecificationException {
        try {
            SpecificationTextValue result = objectMapper.readValue(value, SpecificationTextValue.class);
            if (result == null || result.getName() == null) {
                throw new IllegalProductSpecificationException("纯文本类型的商品规格选项值内容不合法");
            }
        } catch (JsonProcessingException e) {
            throw new IllegalProductSpecificationException("纯文本类型的商品规格选项值内容不合法");
        }
    }

    @Override
    public String getText(String value) throws IllegalProductSpecificationException {
        validate(value);
        try {
            SpecificationTextValue result = objectMapper.readValue(value, SpecificationTextValue.class);
            return result.getName();
        } catch (JsonProcessingException e) {
            // 不做任何事情，因为在validate方法里面判断合法性了
            return null;
        }
    }

    @Data
    private static class SpecificationTextValue {
        private String name;
    }
}
