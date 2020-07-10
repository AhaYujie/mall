package online.ahayujie.mall.admin.pms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author aha
 * @date 2020/7/8
 */
@Data
public class UpdateBrandParam {
    @ApiModelProperty(value = "名称", required = true)
    private String name;

    @ApiModelProperty(value = "首字母")
    private String firstLetter;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "是否为品牌制造商：0->不是；1->是")
    private Integer isFactory;

    @ApiModelProperty(value = "是否显示：0->不显示，1->显示")
    private Integer isShow;

    @ApiModelProperty(value = "品牌logo", required = true)
    private String logo;

    @ApiModelProperty(value = "专区大图")
    private String bigPic;

    @ApiModelProperty(value = "品牌故事")
    private String brandStory;
}
