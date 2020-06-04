package online.ahayujie.mall.common.exception;

import online.ahayujie.mall.common.api.ResultCode;

/**
 * API异常
 * @author aha
 * @date 2020/5/21
 */
public class ApiException extends RuntimeException {

    private ResultCode resultCode;

    public ApiException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(Throwable cause) {
        super(cause);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}
