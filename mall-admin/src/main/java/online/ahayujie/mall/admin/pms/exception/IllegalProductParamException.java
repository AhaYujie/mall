package online.ahayujie.mall.admin.pms.exception;

import online.ahayujie.mall.common.api.ResultCode;
import online.ahayujie.mall.common.exception.ApiException;

/**
 * @author aha
 * @since 2020/7/14
 */
public class IllegalProductParamException extends ApiException {
    public IllegalProductParamException(ResultCode resultCode) {
        super(resultCode);
    }

    public IllegalProductParamException(String message) {
        super(message);
    }

    public IllegalProductParamException(Throwable cause) {
        super(cause);
    }

    public IllegalProductParamException(String message, Throwable cause) {
        super(message, cause);
    }
}
