package com.fatcat.search.controller;

import com.fatcat.search.service.IApiService;
import com.fatcat.user.api.vo.TestTimeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * api 测试
 *
 * @author fatcat
 * @description api
 * @create 2021/4/28
 **/
@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private IApiService apiService;

    /**
     * POST 时间格式转化
     */
    @PostMapping
    public TestTimeVo post(@RequestBody TestTimeVo time) {
        return apiService.post(time);
    }

    /**
     * GET 时间格式转化
     */
    @GetMapping
    public TestTimeVo get(TestTimeVo time) {
        return apiService.get(time);
    }

    /**
     * PUT 时间格式转化
     */
    @PutMapping("/{date}/{localDateTime}/{localDate}/{localTime}")
    public TestTimeVo put(@PathVariable("date") Date date,
                          @PathVariable("localDateTime") LocalDateTime localDateTime,
                          @PathVariable("localDate") LocalDate localDate,
                          @PathVariable("localTime") LocalTime localTime) {
        return apiService.put(date, localDateTime, localDate, localTime);
    }

    @PutMapping("/{date}")
    public TestTimeVo putDate(@PathVariable("date") Date date) {
        return apiService.putDate(date);
    }

    /**
     * DELETE 时间格式转化
     */
    @DeleteMapping("/{date}/{localDateTime}/{localDate}/{localTime}")
    public TestTimeVo delete(@PathVariable("date") Date date,
                             @PathVariable("localDateTime") LocalDateTime localDateTime,
                             @PathVariable("localDate") LocalDate localDate,
                             @PathVariable("localTime") LocalTime localTime) {
        return apiService.delete(date, localDateTime, localDate, localTime);
    }

    @GetMapping("/getDate")
    public TestTimeVo getDate(@RequestParam("localDateTime") LocalDateTime localDateTime) {
        return apiService.getDate(localDateTime);
    }

}
