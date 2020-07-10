package online.ahayujie.mall.admin.pms.exception;

import online.ahayujie.mall.common.api.ResultCode;
import online.ahayujie.mall.common.exception.ApiException;

/**
 * @author aha
 * @date 2020/7/8
 */
public class IllegalBrandException extends ApiException {
    public IllegalBrandException(ResultCode resultCode) {
        super(resultCode);
    }

    public IllegalBrandException(String message) {
        super(message);
    }

    public IllegalBrandException(Throwable cause) {
        super(cause);
    }

    public IllegalBrandException(String message, Throwable cause) {
        super(message, cause);
    }
}
