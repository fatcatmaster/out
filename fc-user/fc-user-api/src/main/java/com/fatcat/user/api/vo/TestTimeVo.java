package com.fatcat.user.api.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * @author fatcat
 * @description 测试时间转换
 * @create 2021/4/23
 **/
@Data
public class TestTimeVo {


    private Date date;

    private LocalDateTime localDateTime;

    private LocalDate localDate;

    private LocalTime localTime;

}
