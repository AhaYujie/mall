package online.ahayujie.mall.admin.ums.exception;

import online.ahayujie.mall.common.api.ResultCode;
import online.ahayujie.mall.common.exception.ApiException;

/**
 * @author aha
 * @date 2020/6/20
 */
public class IllegalResourceException extends ApiException {
    public IllegalResourceException(ResultCode resultCode) {
        super(resultCode);
    }

    public IllegalResourceException(String message) {
        super(message);
    }

    public IllegalResourceException(Throwable cause) {
        super(cause);
    }

    public IllegalResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
