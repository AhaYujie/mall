package online.ahayujie.mall.admin.pms.exception;

import online.ahayujie.mall.common.api.ResultCode;
import online.ahayujie.mall.common.exception.ApiException;

/**
 * @author aha
 * @since 2020/7/14
 */
public class IllegalProductException extends ApiException {
    public IllegalProductException(ResultCode resultCode) {
        super(resultCode);
    }

    public IllegalProductException(String message) {
        super(message);
    }

    public IllegalProductException(Throwable cause) {
        super(cause);
    }

    public IllegalProductException(String message, Throwable cause) {
        super(message, cause);
    }
}
