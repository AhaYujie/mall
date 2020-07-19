package online.ahayujie.mall.admin.pms.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import online.ahayujie.mall.admin.pms.bean.model.ProductSpecificationValue;
import online.ahayujie.mall.admin.pms.exception.IllegalProductSpecificationException;
import online.ahayujie.mall.admin.pms.service.SpecificationValueStrategy;
import org.springframework.stereotype.Service;

/**
 * 图文类型的商品规格选项值策略实现类。
 * json例子：{"name" : "test", "image" : "http://test.jpg"}
 * @author aha
 * @since 2020/7/15
 */
@Service(value = ProductSpecificationValue.IMAGE_TEXT_TYPE_NAME)
public class SpecificationImageTextValueStrategy implements SpecificationValueStrategy {
    private final ObjectMapper objectMapper;

    public SpecificationImageTextValueStrategy(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void validate(String value) throws IllegalProductSpecificationException {
        try {
            SpecificationImageTextValue imageTextValue = objectMapper.readValue(value, SpecificationImageTextValue.class);
            if (imageTextValue == null || imageTextValue.getName() == null || imageTextValue.getImage() == null) {
                throw new IllegalProductSpecificationException("图文类型的商品规格选项值内容不合法");
            }
        } catch (JsonProcessingException e) {
            throw new IllegalProductSpecificationException("图文类型的商品规格选项值内容不合法");
        }
    }

    @Override
    public String getText(String value) throws IllegalProductSpecificationException {
        validate(value);
        try {
            SpecificationImageTextValue imageTextValue = objectMapper.readValue(value, SpecificationImageTextValue.class);
            return imageTextValue.getName();
        } catch (JsonProcessingException e) {
            // 不做任何事情，因为已经在validate方法里检查合法性了
            return null;
        }
    }

    @Data
    private static class SpecificationImageTextValue {
        private String name;
        private String image;
    }
}
