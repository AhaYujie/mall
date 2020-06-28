package online.ahayujie.mall.admin.ums.exception;

import online.ahayujie.mall.common.api.ResultCode;
import online.ahayujie.mall.common.exception.ApiException;

/**
 * @author aha
 * @date 2020/6/21
 */
public class IllegalResourceCategoryException extends ApiException {
    public IllegalResourceCategoryException(ResultCode resultCode) {
        super(resultCode);
    }

    public IllegalResourceCategoryException(String message) {
        super(message);
    }

    public IllegalResourceCategoryException(Throwable cause) {
        super(cause);
    }

    public IllegalResourceCategoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
