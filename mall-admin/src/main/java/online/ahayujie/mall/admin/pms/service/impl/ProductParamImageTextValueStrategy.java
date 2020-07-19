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
 * 图文类型的商品参数值策略实现类
 * @author aha
 * @since 2020/7/15
 */
@Service(value = ProductParam.IMAGE_TEXT_TYPE)
public class ProductParamImageTextValueStrategy implements ProductParamValueStrategy {
    private final ObjectMapper objectMapper;

    public ProductParamImageTextValueStrategy(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void validate(String value) throws IllegalProductParamException {
        try {
            ProductParamImageTextValue imageTextValue = objectMapper.readValue(value, ProductParamImageTextValue.class);
            if (imageTextValue == null || imageTextValue.getName() == null || imageTextValue.getImage() == null) {
                throw new IllegalProductParamException("图文类型的商品参数值不合法：" + value);
            }
        } catch (JsonProcessingException e) {
            throw new IllegalProductParamException("图文类型的商品参数值不合法：" + value);
        }
    }

    @Data
    private static class ProductParamImageTextValue {
        private String name;
        private String image;
    }
}
