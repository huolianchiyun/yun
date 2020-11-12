package com.yun.sys.modules.common.handler;

import com.yun.common.exception.BadRequestException;
import com.yun.common.exception.EntityExistException;
import com.yun.common.exception.EntityNotFoundException;
import com.yun.common.web.response.ResponseData;
import com.yun.common.exception.ThrowableUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.validation.ConstraintViolationException;

import static com.yun.common.web.response.ResponseUtil.*;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * 权限模块全局异常处理
 *
 * 多个全局异常处理器时，先加载的 @ControllerAdvice类里，如果有 @ExceptionHandler(xxException.class)是需要捕获的异常或其父类，
 * 则将使用先加载的类中的异常处理方式。如果没有，则看下一个 @ControllerAdvice类里是否有，以此类推。
 * 异常处理入口：入口 ExceptionHandlerExceptionResolver#doResolveHandlerMethodException。
 * 多个全局异常处理器排序：
 * @see org.springframework.web.method.ControllerAdviceBean#findAnnotatedBeans(org.springframework.context.ApplicationContext)
 * 可以使用 @Order来决定加载优先级
 */

@Slf4j
@Order(HIGHEST_PRECEDENCE)
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
    /**
     * 处理接口验证失败异常
     */
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ResponseData> handleAccessDeniedException(AccessDeniedException e) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        return noApiRights(e.getLocalizedMessage());
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
