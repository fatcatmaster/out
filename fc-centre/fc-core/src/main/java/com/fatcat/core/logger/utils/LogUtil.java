package com.fatcat.core.logger.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fatcat.core.logger.entity.LoggerEntity;
import com.fatcat.core.logger.thread.LogSendThread;
import com.fatcat.core.utils.ApplicationContextUtil;
import com.fatcat.core.utils.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * 对日志的二次封装
 * 实现逻辑：通过rabbit中将日志推送到队列中，异步消费日志消息保存在es中
 *
 * @author fatcat
 * @description 日志记录工具类
 * @create 2021/5/10
 **/
@Slf4j
public class LogUtil {
    /**
     * 引入消息发送模板
     */
    private static RabbitTemplate rabbitTemplate = ApplicationContextUtil.getBean(RabbitTemplate.class);

    public enum LogLevel {
        /**
         * 日志级别，和 log4j 级别保持一致
         */
        TRACE, DEBUG, INFO, WARN, ERROR;
    }

    /**
     * 发送日志
     */
    private static void sendLogMessage(LoggerEntity logger) {
        // 使用线程池异步发送消息，优化性能
        ThreadPoolUtil.THREAD_POOL_EXECUTOR.execute(new LogSendThread(rabbitTemplate, logger));
    }

    /**
     * 封装日志实体
     *
     * @param elements 进程栈信息
     * @return 日志实体
     */
    private static LoggerEntity formatLogger(StackTraceElement[] elements) {
        // 索引 1 对应的是当前方法的名称和类名， 2 是调用方的方法和类名
        StackTraceElement element = elements[2];
        // 调用的类名
        String className = element.getClassName();
        // 调用的方法名
        String methodName = element.getMethodName();
        // 调用的行数
        int lineNumber = element.getLineNumber();
        return new LoggerEntity(className, methodName, lineNumber);
    }

    /**
     * 带格式化的统一记录
     */
    public static void log(LogLevel level, LoggerEntity logger) {
        switch (level) {
            case TRACE:
                log.trace(logger.getLogMessage() + "{}", JSONUtil.toJsonStr(logger));
                break;
            case DEBUG:
                log.debug(logger.getLogMessage() + "{}", JSONUtil.toJsonStr(logger));
                break;
            case INFO:
                log.info(logger.getLogMessage() + "{}", JSONUtil.toJsonStr(logger));
                break;
            case WARN:
                log.warn(logger.getLogMessage() + "{}", JSONUtil.toJsonStr(logger));
                break;
            case ERROR:
                log.error(logger.getLogMessage() + "{}", JSONUtil.toJsonStr(logger));
                break;
            default:
                break;
        }
        sendLogMessage(logger);
    }

    /**
     * 带格式化的跟踪级别日志
     */
    public static void trace(String format, Object... arguments) {
        log.trace(format, arguments);
        LoggerEntity logger = formatLogger(Thread.currentThread().getStackTrace());
        logger.setLevel(LogLevel.TRACE.name());
        logger.setLogMessage(StrUtil.format(format, arguments));
        sendLogMessage(logger);
    }

    /**
     * 带异常的跟踪级别日志
     */
    public static void trace(String msg, Throwable t) {
        log.trace(msg, t);
        LoggerEntity logger = formatLogger(Thread.currentThread().getStackTrace());
        logger.setLevel(LogLevel.TRACE.name());
        logger.setLogMessage(msg);
        logger.setLogDetail(JSONUtil.toJsonStr(t));
        sendLogMessage(logger);
    }

    /**
     * 带格式化的调试级别日志
     */
    public static void debug(String format, Object... arguments) {
        log.debug(format, arguments);
        LoggerEntity logger = formatLogger(Thread.currentThread().getStackTrace());
        logger.setLevel(LogLevel.DEBUG.name());
        logger.setLogMessage(StrUtil.format(format, arguments));
        sendLogMessage(logger);
    }

    /**
     * 带异常的调试级别日志
     */
    public static void debug(String msg, Throwable t) {
        log.debug(msg, t);
        LoggerEntity logger = formatLogger(Thread.currentThread().getStackTrace());
        logger.setLevel(LogLevel.DEBUG.name());
        logger.setLogMessage(msg);
        logger.setLogDetail(JSONUtil.toJsonStr(t));
        sendLogMessage(logger);
    }

    /**
     * 带格式化的普通级别日志
     */
    public static void info(String format, Object... arguments) {
        log.info(format, arguments);
        LoggerEntity logger = formatLogger(Thread.currentThread().getStackTrace());
        logger.setLevel(LogLevel.INFO.name());
        logger.setLogMessage(StrUtil.format(format, arguments));
        sendLogMessage(logger);
    }

    /**
     * 带异常的普通级别日志
     */
    public static void info(String msg, Throwable t) {
        log.info(msg, t);
        LoggerEntity logger = formatLogger(Thread.currentThread().getStackTrace());
        logger.setLevel(LogLevel.INFO.name());
        logger.setLogMessage(msg);
        logger.setLogDetail(JSONUtil.toJsonStr(t));
        sendLogMessage(logger);
    }

    /**
     * 带格式化的警告级别日志
     */
    public static void warn(String format, Object... arguments) {
        log.warn(format, arguments);
        LoggerEntity logger = formatLogger(Thread.currentThread().getStackTrace());
        logger.setLevel(LogLevel.WARN.name());
        logger.setLogMessage(StrUtil.format(format, arguments));
        sendLogMessage(logger);
    }

    /**
     * 带异常的警告级别日志
     */
    public static void warn(String msg, Throwable t) {
        log.warn(msg, t);
        LoggerEntity logger = formatLogger(Thread.currentThread().getStackTrace());
        logger.setLevel(LogLevel.WARN.name());
        logger.setLogMessage(msg);
        logger.setLogDetail(JSONUtil.toJsonStr(t));
        sendLogMessage(logger);
    }

    /**
     * 带格式化的错误级别日志
     */
    public static void error(String format, Object... arguments) {
        log.error(format, arguments);
        LoggerEntity logger = formatLogger(Thread.currentThread().getStackTrace());
        logger.setLevel(LogLevel.ERROR.name());
        logger.setLogMessage(StrUtil.format(format, arguments));
        sendLogMessage(logger);
    }

    /**
     * 带异常的错误级别日志
     */
    public static void error(String msg, Throwable t) {
        log.error(msg, t);
        LoggerEntity logger = formatLogger(Thread.currentThread().getStackTrace());
        logger.setLevel(LogLevel.ERROR.name());
        logger.setLogMessage(msg);
        logger.setLogDetail(JSONUtil.toJsonStr(t));
        sendLogMessage(logger);
    }
}
