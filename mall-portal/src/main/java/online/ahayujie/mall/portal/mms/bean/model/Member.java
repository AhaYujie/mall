package online.ahayujie.mall.portal.mms.bean.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.ToString;
import online.ahayujie.mall.common.bean.model.Base;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 会员表
 * </p>
 *
 * @author aha
 * @since 2020-10-09
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("mms_member")
@ApiModel(value="Member对象", description="会员表")
public class Member extends Base {
    public enum Status {
        /**
         * 禁用
         */
        DISABLE(0),

        /**
         * 启用
         */
        ENABLED(1)
        ;
        private final Integer value;

        Status(Integer value) {
            this.value = value;
        }

        public Integer value() {
            return value;
        }
    }

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "头像")
    private String icon;

    @ApiModelProperty(value = "性别：0->未知；1->男；2->女")
    private Boolean gender;

    @ApiModelProperty(value = "生日")
    private Date birthday;

    @ApiModelProperty(value = "积分")
    private Integer integration;

    @ApiModelProperty(value = "帐号启用状态:0->禁用；1->启用")
    private Integer status;

}
