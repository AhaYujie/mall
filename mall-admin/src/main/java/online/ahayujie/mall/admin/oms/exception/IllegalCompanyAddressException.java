package online.ahayujie.mall.admin.oms.exception;

import online.ahayujie.mall.common.api.ResultCode;
import online.ahayujie.mall.common.exception.ApiException;

/**
 * @author aha
 * @since 2020/8/8
 */
public class IllegalCompanyAddressException extends ApiException {
    public IllegalCompanyAddressException(ResultCode resultCode) {
        super(resultCode);
    }

    public IllegalCompanyAddressException(String message) {
        super(message);
    }

    public IllegalCompanyAddressException(Throwable cause) {
        super(cause);
    }

    public IllegalCompanyAddressException(String message, Throwable cause) {
        super(message, cause);
    }
}
