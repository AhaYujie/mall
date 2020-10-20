package online.ahayujie.mall.portal.pms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author aha
 * @since 2020/10/20
 */
@Data
public class CommentReplayDTO {
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "会员昵称")
    private String memberNickname;

    @ApiModelProperty(value = "会员头像")
    private String memberIcon;

    @ApiModelProperty(value = "回复内容")
    private String content;

    @ApiModelProperty(value = "评论人员类型；0->会员；1->管理员")
    private Integer type;
}
