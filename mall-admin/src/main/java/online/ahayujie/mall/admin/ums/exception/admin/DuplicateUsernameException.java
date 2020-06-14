package online.ahayujie.mall.admin.ums.exception.admin;

import online.ahayujie.mall.common.exception.ApiException;

/**
 * 重复用户名异常
 * @author aha
 * @date 2020/6/5
 */
public class DuplicateUsernameException extends ApiException {
    public DuplicateUsernameException(Throwable cause) {
        super(cause);
    }
}
