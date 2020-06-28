package online.ahayujie.mall.admin.ums.exception;

import online.ahayujie.mall.common.api.ResultCode;
import online.ahayujie.mall.common.exception.ApiException;

/**
 * @author aha
 * @date 2020/6/27
 */
public class IllegalAdminStatusException extends ApiException {
    public IllegalAdminStatusException(ResultCode resultCode) {
        super(resultCode);
    }

    public IllegalAdminStatusException(String message) {
        super(message);
    }

    public IllegalAdminStatusException(Throwable cause) {
        super(cause);
    }

    public IllegalAdminStatusException(String message, Throwable cause) {
        super(message, cause);
    }
}
