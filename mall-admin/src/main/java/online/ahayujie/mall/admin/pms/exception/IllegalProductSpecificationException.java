package online.ahayujie.mall.admin.pms.exception;

import online.ahayujie.mall.common.api.ResultCode;
import online.ahayujie.mall.common.exception.ApiException;

/**
 * @author aha
 * @since 2020/7/14
 */
public class IllegalProductSpecificationException extends ApiException {
    public IllegalProductSpecificationException(ResultCode resultCode) {
        super(resultCode);
    }

    public IllegalProductSpecificationException(String message) {
        super(message);
    }

    public IllegalProductSpecificationException(Throwable cause) {
        super(cause);
    }

    public IllegalProductSpecificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
