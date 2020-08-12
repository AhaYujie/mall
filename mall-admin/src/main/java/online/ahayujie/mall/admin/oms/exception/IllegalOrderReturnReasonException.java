package online.ahayujie.mall.admin.oms.exception;

import online.ahayujie.mall.common.api.ResultCode;
import online.ahayujie.mall.common.exception.ApiException;

/**
 * @author aha
 * @since 2020/8/12
 */
public class IllegalOrderReturnReasonException extends ApiException {
    public IllegalOrderReturnReasonException(ResultCode resultCode) {
        super(resultCode);
    }

    public IllegalOrderReturnReasonException(String message) {
        super(message);
    }

    public IllegalOrderReturnReasonException(Throwable cause) {
        super(cause);
    }

    public IllegalOrderReturnReasonException(String message, Throwable cause) {
        super(message, cause);
    }
}
