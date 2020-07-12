package online.ahayujie.mall.admin.pms.exception;

import online.ahayujie.mall.common.api.ResultCode;
import online.ahayujie.mall.common.exception.ApiException;

/**
 * @author aha
 * @date 2020/7/10
 */
public class IllegalProductCategoryException extends ApiException {
    public IllegalProductCategoryException(ResultCode resultCode) {
        super(resultCode);
    }

    public IllegalProductCategoryException(String message) {
        super(message);
    }

    public IllegalProductCategoryException(Throwable cause) {
        super(cause);
    }

    public IllegalProductCategoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
