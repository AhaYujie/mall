package online.ahayujie.mall.admin.oms.exception;

import online.ahayujie.mall.common.api.ResultCode;
import online.ahayujie.mall.common.exception.ApiException;

/**
 * @author aha
 * @since 2020/8/14
 */
public class IllegalOrderException extends ApiException {
    public IllegalOrderException(ResultCode resultCode) {
        super(resultCode);
    }

    public IllegalOrderException(String message) {
        super(message);
    }

    public IllegalOrderException(Throwable cause) {
        super(cause);
    }

    public IllegalOrderException(String message, Throwable cause) {
        super(message, cause);
    }
}
