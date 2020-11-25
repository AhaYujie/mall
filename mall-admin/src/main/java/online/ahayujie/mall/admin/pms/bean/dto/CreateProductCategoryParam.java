package online.ahayujie.mall.admin.pms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author aha
 * @date 2020/7/10
 */
@Data
public class CreateProductCategoryParam {
    @ApiModelProperty(value = "上级分类的编号：0表示一级分类；默认为0")
    private Long parentId;

    @ApiModelProperty(value = "名称", required = true)
    private String name;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "是否显示在导航栏：0->不显示；1->显示；默认为0")
    private Integer isNav;

    @ApiModelProperty(value = "排序，从大到小排序；默认为0")
    private Integer sort;
}
