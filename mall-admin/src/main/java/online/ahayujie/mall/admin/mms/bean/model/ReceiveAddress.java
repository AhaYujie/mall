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
 * 会员收货地址表
 * </p>
 *
 * @author aha
 * @since 2020-08-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("mms_receive_address")
@ApiModel(value="ReceiveAddress对象", description="会员收货地址表")
public class ReceiveAddress extends Base {
    public static final int NOT_DEFAULT = 0;
    public static final int DEFAULT = 1;

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "会员id")
    private Long memberId;

    @ApiModelProperty(value = "收货人名称")
    private String name;

    @ApiModelProperty(value = "收货人手机号")
    private String phoneNumber;

    @ApiModelProperty(value = "省份/直辖市")
    private String province;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "区")
    private String region;

    @ApiModelProperty(value = "街道")
    private String street;

    @ApiModelProperty(value = "详细地址(街道)")
    private String detailAddress;

    @ApiModelProperty(value = "是否为默认")
    private Integer isDefault;


}
