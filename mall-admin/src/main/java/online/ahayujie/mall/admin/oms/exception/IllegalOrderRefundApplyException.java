package online.ahayujie.mall.admin.oms.exception;

import online.ahayujie.mall.common.api.ResultCode;
import online.ahayujie.mall.common.exception.ApiException;

/**
 * @author aha
 * @since 2020/8/27
 */
public class IllegalOrderRefundApplyException extends ApiException {
    public IllegalOrderRefundApplyException(ResultCode resultCode) {
        super(resultCode);
    }

    public IllegalOrderRefundApplyException(String message) {
        super(message);
    }

    public IllegalOrderRefundApplyException(Throwable cause) {
        super(cause);
    }

    public IllegalOrderRefundApplyException(String message, Throwable cause) {
        super(message, cause);
    }
}
