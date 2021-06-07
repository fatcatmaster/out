package com.fatcat.search.service.impl;

import com.fatcat.search.service.IApiService;
import com.fatcat.user.api.feign.ITestFeignApi;
import com.fatcat.user.api.vo.TestTimeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Service
public class ApiServiceImpl implements IApiService {

    @Autowired
    private ITestFeignApi testService;

    @Override
    public TestTimeVo post(TestTimeVo time) {
        return testService.post(time);
    }

    @Override
    public TestTimeVo get(TestTimeVo time) {
        return testService.get(time);
    }

    @Override
    public TestTimeVo put(Date date, LocalDateTime localDateTime, LocalDate localDate, LocalTime localTime) {
        return testService.put(date, localDateTime, localDate, localTime);
    }

    @Override
    public TestTimeVo putDate(Date date) {
        return testService.putDate(date);
    }

    @Override
    public TestTimeVo delete(Date date, LocalDateTime localDateTime, LocalDate localDate, LocalTime localTime) {
        return testService.delete(date, localDateTime, localDate, localTime);
    }

    @Override
    public TestTimeVo getDate(LocalDateTime localDateTime) {
        return testService.getDate(localDateTime);
    }
}
