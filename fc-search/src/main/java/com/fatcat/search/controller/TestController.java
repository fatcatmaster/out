package com.fatcat.search.controller;

import com.fatcat.search.entity.TestTime;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * 测试时间转换 前端控制器
 * @author fatcat
 * @description 测试时间转换
 * @create 2021/4/23
 **/
@RestController
@RequestMapping("/test")
public class TestController {

    /**
     * POST 时间格式转化
     */
    @PostMapping
    public TestTime post(@RequestBody TestTime time) {
        return time;
    }

    /**
     * GET 时间格式转化
     */
    @GetMapping
    public TestTime get(TestTime time) {
        return time;
    }

    /**
     * PUT 时间格式转化
     */
    @PutMapping("/{date}/{localDateTime}/{localDate}/{localTime}")
    public TestTime put(@PathVariable("date") Date date,
                        @PathVariable("localDateTime") LocalDateTime localDateTime,
                        @PathVariable("localDate") LocalDate localDate,
                        @PathVariable("localTime") LocalTime localTime) {
        TestTime time = new TestTime();
        time.setDate(date);
        time.setLocalDateTime(localDateTime);
        time.setLocalDate(localDate);
        time.setLocalTime(localTime);
        return time;
    }

    /**
     * DELETE 时间格式转化
     */
    @DeleteMapping("/{date}/{localDateTime}/{localDate}/{localTime}")
    public TestTime delete(@PathVariable("date") Date date,
                           @PathVariable("localDateTime") LocalDateTime localDateTime,
                           @PathVariable("localDate") LocalDate localDate,
                           @PathVariable("localTime") LocalTime localTime) {
        TestTime time = new TestTime();
        time.setDate(date);
        time.setLocalDateTime(localDateTime);
        time.setLocalDate(localDate);
        time.setLocalTime(localTime);
        return time;
    }
}
