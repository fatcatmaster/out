package com.fatcat.user.app.controller;

import com.fatcat.core.logger.utils.LogUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 日志测试 前端控制器
 *
 * @author fatcat
 * @description 日志测试
 * @create 2021/5/8
 **/
@RestController
@RequestMapping("/logger")
public class LoggerController {

    /**
     * TRACE 日志测试
     */
    @GetMapping("/trace")
    public void all() {
        LogUtil.trace("This is a trace log!");
    }

    /**
     * DEBUG 日志测试
     */
    @GetMapping("/debug")
    public void debug() {
        LogUtil.debug("This is a debug log!");
    }

    /**
     * INFO 日志测试
     */
    @GetMapping("/info")
    public void info() {
        LogUtil.info("This is a info log!");
    }

    /**
     * WARN 日志测试
     */
    @GetMapping("/warn")
    public void warn() {
        LogUtil.warn("This is a warn log!");
    }

    /**
     * ERROR 日志测试
     */
    @GetMapping("/error")
    public void error() {
        LogUtil.error("This is a error log!");
    }

}
