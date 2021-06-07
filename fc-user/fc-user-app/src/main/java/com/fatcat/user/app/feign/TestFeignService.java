package com.fatcat.user.app.feign;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.fatcat.user.api.feign.ITestFeignApi;
import com.fatcat.user.api.vo.TestTimeVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * @author fatcat
 * @description 测试 feign实现体
 * @create 2021/4/27
 **/
@RestController
@RequestMapping("/api/test")
public class TestFeignService implements ITestFeignApi {

    /**
     * POST 时间格式转化
     */
    @Override
    public TestTimeVo post(TestTimeVo time) {
        return time;
    }

    /**
     * GET 时间格式转化
     */
    @Override
    public TestTimeVo get(TestTimeVo time) {
        return time;
    }

    /**
     * PUT 时间格式转化
     */
    @Override
    public TestTimeVo put(Date date,
                          LocalDateTime localDateTime,
                          LocalDate localDate,
                          LocalTime localTime) {
        TestTimeVo time = new TestTimeVo();
        time.setDate(date);
        time.setLocalDateTime(localDateTime);
        time.setLocalDate(localDate);
        time.setLocalTime(localTime);
        return time;
    }

    /**
     * DELETE 时间格式转化
     */
    @Override
    public TestTimeVo delete(Date date,
                             LocalDateTime localDateTime,
                             LocalDate localDate,
                             LocalTime localTime) {
        TestTimeVo time = new TestTimeVo();
        time.setDate(date);
        time.setLocalDateTime(localDateTime);
        time.setLocalDate(localDate);
        time.setLocalTime(localTime);
        return time;
    }

    @Override
    public TestTimeVo putDate(Date date) {
        TestTimeVo time = new TestTimeVo();
        time.setDate(date);
        time.setLocalDateTime(LocalDateTimeUtil.now());
        return time;
    }

    @Override
    public TestTimeVo getDate(LocalDateTime localDateTime) {
        TestTimeVo time = new TestTimeVo();
        time.setDate(DateUtil.date());
        time.setLocalDateTime(localDateTime);
        return time;
    }
}
