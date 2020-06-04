package online.ahayujie.mall.common.exception;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.common.api.Result;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 * @author aha
 * @date 2020/3/26
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Object> handleApiException(ApiException e) {
        e.printStackTrace();
        if (e.getResultCode() != null) {
            return Result.fail(e.getResultCode());
        }
        return Result.fail(e.getMessage());
    }

}
