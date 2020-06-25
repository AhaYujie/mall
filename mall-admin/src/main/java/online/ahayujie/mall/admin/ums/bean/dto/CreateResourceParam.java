package online.ahayujie.mall.admin.ums.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author aha
 * @date 2020/6/25
 */
@Data
public class CreateResourceParam {
    @ApiModelProperty(value = "资源名称", required = true)
    private String name;

    @ApiModelProperty(value = "资源URL", required = true)
    private String url;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "资源分类ID")
    private Long categoryId;
}
