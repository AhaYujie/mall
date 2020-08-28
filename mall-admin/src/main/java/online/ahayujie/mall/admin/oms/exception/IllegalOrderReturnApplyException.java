package online.ahayujie.mall.admin.oms.exception;

import online.ahayujie.mall.common.api.ResultCode;
import online.ahayujie.mall.common.exception.ApiException;

/**
 * @author aha
 * @since 2020/8/28
 */
public class IllegalOrderReturnApplyException extends ApiException {
    public IllegalOrderReturnApplyException(ResultCode resultCode) {
        super(resultCode);
    }

    /**
     * {@link ResultCode} 默认为 {@code online.ahayujie.mall.common.api.ResultCodeEnum.FAIL}
     *
     * @param message 错误信息
     */
    public IllegalOrderReturnApplyException(String message) {
        super(message);
    }

    /**
     * {@link ResultCode} 默认为 {@code online.ahayujie.mall.common.api.ResultCodeEnum.FAIL}
     *
     * @param cause 错误原因
     */
    public IllegalOrderReturnApplyException(Throwable cause) {
        super(cause);
    }

    /**
     * {@link ResultCode} 默认为 {@code online.ahayujie.mall.common.api.ResultCodeEnum.FAIL}
     *
     * @param message 错误信息
     * @param cause   错误原因
     */
    public IllegalOrderReturnApplyException(String message, Throwable cause) {
        super(message, cause);
    }
}
