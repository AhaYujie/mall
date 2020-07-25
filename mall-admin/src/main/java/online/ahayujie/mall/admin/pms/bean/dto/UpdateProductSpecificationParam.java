package online.ahayujie.mall.admin.pms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author aha
 * @since 2020/7/22
 */
@Data
public class UpdateProductSpecificationParam {
    @ApiModelProperty(value = "商品规格列表", notes = "只能对已有的商品规格新增选项")
    private List<UpdateSpecification> specifications;

    @Data
    public static class UpdateSpecification {
        @ApiModelProperty(value = "规格id")
        private Long id;

        @ApiModelProperty(value = "规格选项")
        private List<UpdateSpecificationValue> values;
    }

    @Data
    public static class UpdateSpecificationValue {
        @ApiModelProperty(value = "选项值")
        private String value;

        @ApiModelProperty(value = "选项类型：0->纯文本，1->图文")
        private Integer type;
    }
}
