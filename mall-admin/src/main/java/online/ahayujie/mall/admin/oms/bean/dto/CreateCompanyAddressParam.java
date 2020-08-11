package online.ahayujie.mall.admin.oms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author aha
 * @since 2020/8/8
 */
@Data
public class CreateCompanyAddressParam {
    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "收发货人姓名")
    private String receiverName;

    @ApiModelProperty(value = "收发货人电话")
    private String receiverPhone;

    @ApiModelProperty(value = "省/直辖市")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区")
    private String region;

    @ApiModelProperty(value = "街道")
    private String street;

    @ApiModelProperty(value = "详细地址")
    private String detailAddress;

    @ApiModelProperty(value = "是否默认发货地址：0->否；1->是；默认为0")
    private Integer isSendDefault;

    @ApiModelProperty(value = "是否默认收货地址：0->否；1->是；默认为0")
    private Integer isReceiveDefault;

    @ApiModelProperty(value = "启用状态；0->未启用；1->已启用；默认为0")
    private Integer status;
}
