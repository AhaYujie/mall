package online.ahayujie.mall.admin.oms.exception;

import online.ahayujie.mall.common.api.ResultCode;
import online.ahayujie.mall.common.exception.ApiException;

/**
 * @author aha
 * @since 2020/8/11
 */
public class IllegalOrderRefundReasonException extends ApiException {
    public IllegalOrderRefundReasonException(ResultCode resultCode) {
        super(resultCode);
    }

    public IllegalOrderRefundReasonException(String message) {
        super(message);
    }

    public IllegalOrderRefundReasonException(Throwable cause) {
        super(cause);
    }

    public IllegalOrderRefundReasonException(String message, Throwable cause) {
        super(message, cause);
    }
}
