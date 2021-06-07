package com.fatcat.generator.convert;

import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.fatcat.generator.constants.BaseFieldType;
import com.fatcat.generator.constants.MySqlFieldType;

/**
 * @author zhouhc
 * @description mysql 数据库自定义类型转换
 * @create 2021/5/21
 * @see com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert
 * @see com.baomidou.mybatisplus.generator.config.ITypeConvert
 * @see com.baomidou.mybatisplus.generator.config.rules.DbColumnType
 * {@link com.baomidou.mybatisplus.generator.config.GlobalConfig#setDateType} 默认设置其他时间类型为指定类型
 **/
public class MySqlConvert extends MySqlTypeConvert {

    public final static MySqlConvert INSTANCE = new MySqlConvert();

    @Override
    public IColumnType processTypeConvert(GlobalConfig config, String fieldType) {
        if (containsAny(fieldType, MySqlFieldType.DATETIME)) {
            // 所有时间类型均使用 java.util.Date
            return DbColumnType.DATE;
        } else {
            return super.processTypeConvert(config, fieldType);
        }
    }

    private boolean containsAny(String fieldType, BaseFieldType... types) {
        fieldType = fieldType.toLowerCase();
        for (BaseFieldType type : types) {
            if (fieldType.contains(type.getType())) {
                return true;
            }
        }
        return false;
    }

}
