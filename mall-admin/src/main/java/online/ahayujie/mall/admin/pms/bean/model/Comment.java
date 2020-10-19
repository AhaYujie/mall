package online.ahayujie.mall.admin.pms.bean.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import online.ahayujie.mall.common.bean.model.Base;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品评价表
 * </p>
 *
 * @author aha
 * @since 2020-10-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pms_comment")
@ApiModel(value="Comment对象", description="商品评价表")
public class Comment extends Base {
    public static final int HIDE = 0;
    public static final int SHOW = 1;

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品id")
    private Long productId;

    @ApiModelProperty(value = "会员id")
    private Long memberId;

    @ApiModelProperty(value = "会员昵称")
    private String memberNickname;

    @ApiModelProperty(value = "评论用户头像")
    private String memberIcon;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "商品规格（json格式）")
    private String specification;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "上传图片地址，以逗号隔开")
    private String pics;

    @ApiModelProperty(value = "评价星数：0->5")
    private Integer star;

    @ApiModelProperty(value = "点赞数")
    private Integer likeCount;

    @ApiModelProperty(value = "阅读数")
    private Integer readCount;

    @ApiModelProperty(value = "回复数")
    private Integer replayCount;

    @ApiModelProperty(value = "是否显示，0->不显示，1->显示")
    private Integer isShow;

}
