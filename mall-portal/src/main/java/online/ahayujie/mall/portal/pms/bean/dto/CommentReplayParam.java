package online.ahayujie.mall.portal.pms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author aha
 * @since 2020/10/21
 */
@Data
public class CommentReplayParam {
    @ApiModelProperty(value = "评论id")
    private Long commentId;

    @ApiModelProperty(value = "回复内容")
    private String content;
}
