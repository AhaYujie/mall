package online.ahayujie.mall.common.exception;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.common.api.Result;
import online.ahayujie.mall.common.api.ResultCodeEnum;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<String> messages = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                messages.add(error.getDefaultMessage());
            }
        }
        return Result.fail(ResultCodeEnum.BAD_REQUEST, messages.toString());
    }

}
