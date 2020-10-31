package online.ahayujie.mall.search.bean.dto;

import lombok.Data;

import java.util.List;

/**
 * @author aha
 * @since 2020/10/28
 */
@Data
public class ProductSpecificationDTO {
    private String name;

    private List<SpecificationValueDTO> values;

    @Data
    public static class SpecificationValueDTO {
        private String value;
        private Integer type;
    }
}
