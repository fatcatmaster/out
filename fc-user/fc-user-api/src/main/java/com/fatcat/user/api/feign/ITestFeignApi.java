package com.fatcat.user.api.feign;

import com.fatcat.user.api.vo.TestTimeVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * feignClient 参数说明：
 * contextId: 当有多个feign注册至同一个服务时，需要指定 id
 * name: 实现当前接口的服务名称
 * path: 当前请求的路径
 *
 * @author fatcat
 * @description 测试 feign
 * @create 2021/4/27
 **/
@FeignClient(contextId = "test", name = "fc-user", path = "fc-user/api/test")
public interface ITestFeignApi {

    /**
     * POST 时间格式转化
     */
    @PostMapping
    TestTimeVo post(@RequestBody TestTimeVo time);

    /**
     * GET 时间格式转化
     */
    @GetMapping
    TestTimeVo get(TestTimeVo time);

    /**
     * PUT 时间格式转化
     */
    @PutMapping("/{date}/{localDateTime}/{localDate}/{localTime}")
    TestTimeVo put(@PathVariable("date") Date date,
                   @PathVariable("localDateTime") LocalDateTime localDateTime,
                   @PathVariable("localDate") LocalDate localDate,
                   @PathVariable("localTime") LocalTime localTime);

    /**
     * DELETE 时间格式转化
     */
    @DeleteMapping("/{date}/{localDateTime}/{localDate}/{localTime}")
    TestTimeVo delete(@PathVariable("date") Date date,
                      @PathVariable("localDateTime") LocalDateTime localDateTime,
                      @PathVariable("localDate") LocalDate localDate,
                      @PathVariable("localTime") LocalTime localTime);

    @PutMapping("/{date}")
    TestTimeVo putDate(@PathVariable("date") Date date);

    @GetMapping("/getDate")
    TestTimeVo getDate(@RequestParam("localDateTime") LocalDateTime localDateTime);
}
