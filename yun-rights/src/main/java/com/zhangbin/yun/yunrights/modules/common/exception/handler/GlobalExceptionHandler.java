package com.zhangbin.yun.yunrights.modules.common.exception.handler;

import com.zhangbin.yun.yunrights.modules.common.exception.BadRequestException;
import com.zhangbin.yun.yunrights.modules.common.exception.EntityExistException;
import com.zhangbin.yun.yunrights.modules.common.exception.EntityNotFoundException;
import com.zhangbin.yun.yunrights.modules.common.response.ResponseData;

import static com.zhangbin.yun.yunrights.modules.common.response.ResponseUtil.error;
import static com.zhangbin.yun.yunrights.modules.common.response.ResponseUtil.requestError;

import com.zhangbin.yun.yunrights.modules.common.utils.ThrowableUtil;
import com.zhangbin.yun.yunrights.modules.common.utils.ValidationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.Objects;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理所有不可知的异常
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ResponseData> handleException(Throwable e) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        return error(e.getMessage());
    }

    /**
     * BadCredentialsException
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseData> badCredentialsException(BadCredentialsException e) {
        // 打印堆栈信息
        String message = "坏的凭证".equals(e.getMessage()) ? "用户名或密码不正确" : e.getMessage();
        log.error(message);
        return error(message);
    }

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<ResponseData> badRequestException(BadRequestException e) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        return error(e.getMessage());
    }

    /**
     * 处理 EntityExist
     */
    @ExceptionHandler(value = EntityExistException.class)
    public ResponseEntity<ResponseData> entityExistException(EntityExistException e) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        return error(e.getMessage());
    }

    /**
     * 处理 EntityNotFound
     */
    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<ResponseData> entityNotFoundException(EntityNotFoundException e) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        return error(e.getMessage());
    }

    /**
     * 处理所有接口数据验证异常
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ResponseData> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        return requestError(getErrMessage(e.getBindingResult()));
    }

    /**
     * 处理所有接口数据验证异常
     */
    @ExceptionHandler({BindException.class})
    public ResponseEntity<ResponseData> handleBindException(BindException e) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        return requestError(getErrMessage(e.getBindingResult()));
    }

    /**
     * 处理所有接口数据验证异常
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ResponseData> handleConstraintViolationException(ConstraintViolationException e) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        return requestError(e.getLocalizedMessage());
    }

    private static String getErrMessage(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .reduce((m1, m2) -> m1 + "；" + m2)
                    .orElse("参数输入错误！");
        }
        return "";
    }
}
