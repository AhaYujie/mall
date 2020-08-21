package online.ahayujie.mall.admin.mms.bean.model;

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
 * 会员登录记录
 * </p>
 *
 * @author aha
 * @since 2020-08-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("mms_login_log")
@ApiModel(value="LoginLog对象", description="会员登录记录")
public class LoginLog extends Base {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "会员id")
    private Long memberId;

    @ApiModelProperty(value = "ip地址")
    private String ip;

    @ApiModelProperty(value = "登录类型：0->PC；1->android;2->ios;3->小程序")
    private Integer loginType;


}
