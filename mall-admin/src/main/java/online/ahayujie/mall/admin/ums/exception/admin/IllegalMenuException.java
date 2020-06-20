package online.ahayujie.mall.admin.ums.exception.admin;

import online.ahayujie.mall.common.api.ResultCode;
import online.ahayujie.mall.common.exception.ApiException;

/**
 * @author aha
 * @date 2020/6/19
 */
public class IllegalMenuException extends ApiException {
    public IllegalMenuException(ResultCode resultCode) {
        super(resultCode);
    }

    public IllegalMenuException(String message) {
        super(message);
    }

    public IllegalMenuException(Throwable cause) {
        super(cause);
    }

    public IllegalMenuException(String message, Throwable cause) {
        super(message, cause);
    }
}
