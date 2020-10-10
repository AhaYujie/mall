package online.ahayujie.mall.portal.mms.exception;

import online.ahayujie.mall.common.api.ResultCode;
import online.ahayujie.mall.common.exception.ApiException;

/**
 * @author aha
 * @since 2020/10/9
 */
public class DuplicatePhoneException extends ApiException {
    public DuplicatePhoneException(ResultCode resultCode) {
        super(resultCode);
    }

    public DuplicatePhoneException(String message) {
        super(message);
    }

    public DuplicatePhoneException(Throwable cause) {
        super(cause);
    }

    public DuplicatePhoneException(String message, Throwable cause) {
        super(message, cause);
    }
}
