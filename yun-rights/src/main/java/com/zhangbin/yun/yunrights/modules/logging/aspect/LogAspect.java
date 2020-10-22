package com.zhangbin.yun.yunrights.modules.logging.aspect;

import com.zhangbin.yun.yunrights.modules.common.utils.IPUtil;
import com.zhangbin.yun.common.spring.RequestHolder;
import com.zhangbin.yun.common.exception.ThrowableUtil;
import static com.zhangbin.yun.yunrights.modules.logging.enums.LogLevel.*;
import com.zhangbin.yun.yunrights.modules.logging.model.$do.LogDO;
import com.zhangbin.yun.yunrights.modules.logging.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public final class LogAspect {

    private final LogService logService;

    ThreadLocal<Long> currentTime = new ThreadLocal<>();


    /**
     * 配置切入点
     */
    @Pointcut("@annotation(com.zhangbin.yun.yunrights.modules.logging.annotation.Logging)")
    public void logPointcut() {
        // 该方法无方法体,主要为了让同类中其他方法使用此切入点
    }

    /**
     * 配置环绕通知,使用在方法logPointcut()上注册的切入点
     *
     * @param joinPoint join point for advice
     */
    @Around("logPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        currentTime.set(System.currentTimeMillis());
        Object result = joinPoint.proceed();
        LogDO log = new LogDO(INFO.getLevel(), System.currentTimeMillis() - currentTime.get());
        currentTime.remove();
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        logService.saveLog(IPUtil.getBrowser(request), IPUtil.getIp(request), joinPoint, log);
        return result;
    }

    /**
     * 配置异常通知
     *
     * @param joinPoint join point for advice
     * @param e         exception
     */
    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        LogDO log = new LogDO(ERROR.getLevel(), System.currentTimeMillis() - currentTime.get());
        currentTime.remove();
        log.setExceptionDetail(ThrowableUtil.getStackTrace(e).getBytes());
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        logService.saveLog(IPUtil.getBrowser(request), IPUtil.getIp(request), (ProceedingJoinPoint) joinPoint, log);
    }
}
