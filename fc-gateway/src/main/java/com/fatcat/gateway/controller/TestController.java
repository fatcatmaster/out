package com.fatcat.gateway.controller;

import com.fatcat.gateway.service.TestService;
import com.fatcat.user.api.vo.TestTimeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * 网关请求测试 前端控制器
 *
 * @author fatcat
 * @description 网关请求测试 前端控制器
 * @create 2021/4/27
 **/
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;

    /**
     * POST 时间格式转化
     */
    @PostMapping
    public TestTimeVo post(@RequestBody TestTimeVo time) {
        return testService.post(time);
    }

    /**
     * GET 时间格式转化
     */
    @GetMapping
    public TestTimeVo get(TestTimeVo time) {
        return testService.get(time);
    }

    /**
     * PUT 时间格式转化
     */
    @PutMapping("/{date}/{localDateTime}/{localDate}/{localTime}")
    public TestTimeVo put(@PathVariable("date") Date date,
                          @PathVariable("localDateTime") LocalDateTime localDateTime,
                          @PathVariable("localDate") LocalDate localDate,
                          @PathVariable("localTime") LocalTime localTime) {
        return testService.put(date, localDateTime, localDate, localTime);
    }

    @PutMapping("/{date}")
    public TestTimeVo putDate(@PathVariable("date") Date date) {
        return testService.putDate(date);
    }

    /**
     * DELETE 时间格式转化
     */
    @DeleteMapping("/{date}/{localDateTime}/{localDate}/{localTime}")
    public TestTimeVo delete(@PathVariable("date") Date date,
                             @PathVariable("localDateTime") LocalDateTime localDateTime,
                             @PathVariable("localDate") LocalDate localDate,
                             @PathVariable("localTime") LocalTime localTime) {
        return testService.delete(date, localDateTime, localDate, localTime);
    }

    @GetMapping("/getDate")
    public TestTimeVo getDate(@RequestParam("localDateTime") LocalDateTime localDateTime){
        return testService.getDate(localDateTime);
    }


}
