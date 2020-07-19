package online.ahayujie.mall.common.exception;

import online.ahayujie.mall.common.api.ResultCode;
import online.ahayujie.mall.common.api.ResultCodeEnum;

/**
 * API异常
 * @author aha
 * @date 2020/5/21
 */
public class ApiException extends RuntimeException {

    private final ResultCode resultCode;

    public ApiException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    /**
     * {@link ResultCode} 默认为 {@code online.ahayujie.mall.common.api.ResultCodeEnum.FAIL}
     * @param message 错误信息
     */
    public ApiException(String message) {
        super(message);
        this.resultCode = ResultCodeEnum.FAIL;
    }

    /**
     * {@link ResultCode} 默认为 {@code online.ahayujie.mall.common.api.ResultCodeEnum.FAIL}
     * @param cause 错误原因
     */
    public ApiException(Throwable cause) {
        super(cause);
        this.resultCode = ResultCodeEnum.FAIL;
    }

    /**
     * {@link ResultCode} 默认为 {@code online.ahayujie.mall.common.api.ResultCodeEnum.FAIL}
     * @param message 错误信息
     * @param cause 错误原因
     */
    public ApiException(String message, Throwable cause) {
        super(message, cause);
        this.resultCode = ResultCodeEnum.FAIL;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}
