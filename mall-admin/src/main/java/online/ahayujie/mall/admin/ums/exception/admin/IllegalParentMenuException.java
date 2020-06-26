package online.ahayujie.mall.admin.ums.exception.admin;

import online.ahayujie.mall.common.api.ResultCode;
import online.ahayujie.mall.common.exception.ApiException;

/**
 * @author aha
 * @date 2020/6/25
 */
public class IllegalParentMenuException extends ApiException {
    public IllegalParentMenuException(ResultCode resultCode) {
        super(resultCode);
    }

    public IllegalParentMenuException(String message) {
        super(message);
    }

    public IllegalParentMenuException(Throwable cause) {
        super(cause);
    }

    public IllegalParentMenuException(String message, Throwable cause) {
        super(message, cause);
    }
}
