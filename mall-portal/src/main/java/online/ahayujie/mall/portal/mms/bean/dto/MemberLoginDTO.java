package online.ahayujie.mall.portal.mms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author aha
 * @since 2020/10/9
 */
@Data
public class MemberLoginDTO {
    @ApiModelProperty(value = "accessToken")
    private String accessToken;

    @ApiModelProperty(value = "refreshToken")
    private String refreshToken;

    @ApiModelProperty(value = "accessToken过期时间")
    private Long expireIn;
}
