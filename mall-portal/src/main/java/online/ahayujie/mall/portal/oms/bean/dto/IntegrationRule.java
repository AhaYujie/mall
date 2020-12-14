package online.ahayujie.mall.portal.oms.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author aha
 * @since 2020/11/10
 */
@Data
public class IntegrationRule {
    @ApiModelProperty(value = "积分和金额的比例，即多少积分可以抵扣一元")
    private BigDecimal integrationRatio;

    @ApiModelProperty(value = "使用积分的最小单位，即使用的积分需要是最小单位的倍数")
    private Integer integrationUseUnit;
}
