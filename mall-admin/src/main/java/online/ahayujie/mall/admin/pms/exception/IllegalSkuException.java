package online.ahayujie.mall.admin.pms.exception;

import online.ahayujie.mall.common.api.ResultCode;
import online.ahayujie.mall.common.exception.ApiException;

/**
 * @author aha
 * @since 2020/7/14
 */
public class IllegalSkuException extends ApiException {
    public IllegalSkuException(ResultCode resultCode) {
        super(resultCode);
    }

    public IllegalSkuException(String message) {
        super(message);
    }

    public IllegalSkuException(Throwable cause) {
        super(cause);
    }

    public IllegalSkuException(String message, Throwable cause) {
        super(message, cause);
    }
}
