package online.ahayujie.mall.admin.ums.exception;

import online.ahayujie.mall.common.api.ResultCode;
import online.ahayujie.mall.common.exception.ApiException;

/**
 * @author aha
 * @date 2020/6/25
 */
public class IllegalMenuVisibilityException extends ApiException {
    public IllegalMenuVisibilityException(ResultCode resultCode) {
        super(resultCode);
    }

    public IllegalMenuVisibilityException(String message) {
        super(message);
    }

    public IllegalMenuVisibilityException(Throwable cause) {
        super(cause);
    }

    public IllegalMenuVisibilityException(String message, Throwable cause) {
        super(message, cause);
    }
}
