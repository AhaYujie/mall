package online.ahayujie.mall.portal.mms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author aha
 * @since 2020/10/11
 */
@Data
public class CreateReceiveAddressParam {
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

    @ApiModelProperty(value = "是否为默认，0->不是，1->是")
    private Integer isDefault;
}
