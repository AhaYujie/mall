package online.ahayujie.mall.admin.oms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author aha
 * @since 2020/10/6
 */
@Data
public class AutoCommentSettingDTO {
    @ApiModelProperty(value = "自动评价的评价内容")
    private String content;

    @ApiModelProperty(value = "自动评价的评价图片，逗号隔开")
    private String pics;

    @ApiModelProperty(value = "自动评价的评价星数")
    private Integer star;
}
