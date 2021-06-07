package com.fatcat.core.logger.aop;

import cn.hutool.json.JSONUtil;
import com.fatcat.core.logger.entity.LoggerEntity;
import com.fatcat.core.logger.utils.LogUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author fatcat
 * @description 记录全局接口请求方法的日志
 * @create 2021/5/10
 **/
@Aspect
public class LoggerAop {

    enum LoggerAopEnum {
        /**
         * 日志类型，请求|响应
         */
        REQUEST_LOGGER, RESPONSE_LOGGER;
    }

    /**
     * 添加注解切点
     */
    @Pointcut(value = "@annotation(com.fatcat.core.logger.annotation.Logger)")
    public void loggerCut() {
    }

    /**
     * 添加工程下面所有 controller 方法
     */
    @Pointcut(value = "execution(* com.fatcat..*.controller..*.*(..))")
    public void controllerCut() {
    }

    /**
     * 添加 api 工程的 feign 切点，不要切接口类，切实现体
     * 如果存在服务之间 feign 调用会打印多份类似的日志
     */
    @Pointcut(value = "execution(* com.fatcat.*.app.feign..*.*(..))")
    public void feignCut() {
    }

    /**
     * 切点的业务逻辑
     */
    @Around("loggerCut() || controllerCut() || feignCut()")
    public Object execution(ProceedingJoinPoint joinPoint) throws Throwable {
        LoggerEntity logger = new LoggerEntity();
        Signature signature = joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        String sourceName = signature.getDeclaringTypeName();
        String methodName = signature.getName();
        // 记录成跟踪日志，容易筛选出这类日志用于跟踪问题
        logger.setLevel(LogUtil.LogLevel.INFO.name());
        logger.setPackageName(sourceName);
        logger.setMethodName(methodName);
        logger.setSystemLog(Boolean.TRUE);
        // 记录请求参数的日志
        logger.setRequestParam(JSONUtil.toJsonStr(args));
        logger.setLogMessage(LoggerAopEnum.REQUEST_LOGGER.name());
        LogUtil.log(LogUtil.LogLevel.INFO, logger);
        // 业务方法的执行
        Object result = joinPoint.proceed(joinPoint.getArgs());
        // 记录返回结果的日志， 因为程序执行可能发生异常，所以请求日志和响应日志分开打印
        logger.setResponseResult(JSONUtil.toJsonStr(result));
        logger.setLogMessage(LoggerAopEnum.RESPONSE_LOGGER.name());
        LogUtil.log(LogUtil.LogLevel.INFO, logger);
        return result;
    }
}
