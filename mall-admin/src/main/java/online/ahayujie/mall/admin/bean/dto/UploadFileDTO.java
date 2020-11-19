package online.ahayujie.mall.admin.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author aha
 * @since 2020/11/19
 */
@Data
public class UploadFileDTO {
    @ApiModelProperty(value = "文件的url")
    private String url;
}
