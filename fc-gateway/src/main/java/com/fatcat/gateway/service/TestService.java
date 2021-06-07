package com.fatcat.gateway.service;

import com.fatcat.user.api.feign.ITestFeignApi;
import com.fatcat.user.api.vo.TestTimeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * @author fatcat
 * @description 测试请求实现体
 * @create 2021/4/27
 **/
@Service
public class TestService {

    @Autowired
    private ITestFeignApi testFeignApi;

    public TestTimeVo post(TestTimeVo time) {
        return testFeignApi.post(time);
    }

    public TestTimeVo get(TestTimeVo time) {
        return testFeignApi.get(time);
    }

    public TestTimeVo put(Date date, LocalDateTime localDateTime, LocalDate localDate, LocalTime localTime) {
        return testFeignApi.put(date, localDateTime, localDate, localTime);
    }

    public TestTimeVo delete(Date date, LocalDateTime localDateTime, LocalDate localDate, LocalTime localTime) {
        return testFeignApi.delete(date, localDateTime, localDate, localTime);
    }

    public TestTimeVo putDate(Date date) {
        return testFeignApi.putDate(date);
    }

    public TestTimeVo getDate(LocalDateTime localDateTime) {
        return testFeignApi.getDate(localDateTime);
    }
}
