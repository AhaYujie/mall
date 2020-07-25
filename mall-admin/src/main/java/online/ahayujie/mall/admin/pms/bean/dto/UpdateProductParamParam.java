package online.ahayujie.mall.admin.pms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author aha
 * @since 2020/7/21
 */
@Data
public class UpdateProductParamParam {
    @ApiModelProperty(value = "商品参数列表", notes = "修改的参数传id，新增的参数不传id，默认剩下不传的是要删除的参数")
    private List<UpdateProductParam> productParams;

    @Data
    public static class UpdateProductParam {
        @ApiModelProperty(value = "参数id")
        private Long id;

        @ApiModelProperty(value = "参数名称")
        private String name;

        @ApiModelProperty(value = "参数值")
        private String value;

        @ApiModelProperty(value = "参数类型：0->纯文本，1->图文")
        private Integer type;
    }
}
