package online.ahayujie.mall.portal.mms.exception;

import online.ahayujie.mall.common.api.ResultCode;
import online.ahayujie.mall.common.exception.ApiException;

/**
 * @author aha
 * @since 2020/10/9
 */
public class DuplicateUsernameException extends ApiException {
    public DuplicateUsernameException(ResultCode resultCode) {
        super(resultCode);
    }

    public DuplicateUsernameException(String message) {
        super(message);
    }

    public DuplicateUsernameException(Throwable cause) {
        super(cause);
    }

    public DuplicateUsernameException(String message, Throwable cause) {
        super(message, cause);
    }
}
