package online.ahayujie.mall.admin.oms.bean.model;

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
 * 公司收发货地址表
 * </p>
 *
 * @author aha
 * @since 2020-08-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("oms_company_address")
@ApiModel(value="CompanyAddress对象", description="公司收发货地址表")
public class CompanyAddress extends Base {
    public static final int NOT_SEND_DEFAULT = 0;
    public static final int SEND_DEFAULT = 1;

    public static final int NOT_RECEIVE_DEFAULT = 0;
    public static final int RECEIVE_DEFAULT = 1;

    public static final int NOT_ACTIVE_STATUS = 0;
    public static final int ACTIVE_STATUS = 1;

    private static final long serialVersionUID = 1L;

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

    @ApiModelProperty(value = "是否默认发货地址：0->否；1->是")
    private Integer isSendDefault;

    @ApiModelProperty(value = "是否默认收货地址：0->否；1->是")
    private Integer isReceiveDefault;

    @ApiModelProperty(value = "启用状态；0->未启用；1->已启用")
    private Integer status;


}
