package online.ahayujie.mall.admin.ums.exception.admin;

import online.ahayujie.mall.common.api.ResultCode;
import online.ahayujie.mall.common.exception.ApiException;

/**
 * @author aha
 * @date 2020/6/19
 */
public class IllegalRoleException extends ApiException {
    public IllegalRoleException(ResultCode resultCode) {
        super(resultCode);
    }

    public IllegalRoleException(String message) {
        super(message);
    }

    public IllegalRoleException(Throwable cause) {
        super(cause);
    }

    public IllegalRoleException(String message, Throwable cause) {
        super(message, cause);
    }
}
