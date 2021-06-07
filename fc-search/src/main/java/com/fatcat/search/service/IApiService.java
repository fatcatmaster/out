package com.fatcat.search.service;

import com.fatcat.user.api.vo.TestTimeVo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public interface IApiService {

    TestTimeVo post(TestTimeVo time);

    TestTimeVo get(TestTimeVo time);

    TestTimeVo put(Date date, LocalDateTime localDateTime, LocalDate localDate, LocalTime localTime);

    TestTimeVo putDate(Date date);

    TestTimeVo delete(Date date, LocalDateTime localDateTime, LocalDate localDate, LocalTime localTime);

    TestTimeVo getDate(LocalDateTime localDateTime);
}
